/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.dubbo.rpc.cluster.support;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.Version;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.common.utils.NetUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.RpcInvocation;
import org.apache.dubbo.rpc.cluster.Directory;
import org.apache.dubbo.rpc.cluster.LoadBalance;
import org.apache.dubbo.rpc.support.RpcUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.apache.dubbo.rpc.cluster.Constants.CLUSTER_AVAILABLE_CHECK_KEY;
import static org.apache.dubbo.rpc.cluster.Constants.CLUSTER_STICKY_KEY;
import static org.apache.dubbo.rpc.cluster.Constants.DEFAULT_CLUSTER_AVAILABLE_CHECK;
import static org.apache.dubbo.rpc.cluster.Constants.DEFAULT_CLUSTER_STICKY;
import static org.apache.dubbo.rpc.cluster.Constants.DEFAULT_LOADBALANCE;
import static org.apache.dubbo.rpc.cluster.Constants.LOADBALANCE_KEY;

/**
 * AbstractClusterInvoker
 */
public abstract class AbstractClusterInvoker<T> implements Invoker<T> {
	
	private static final Logger logger = LoggerFactory.getLogger(AbstractClusterInvoker.class);
	
	protected final Directory<T> directory;
	
	protected final boolean availablecheck;
	
	private AtomicBoolean destroyed = new AtomicBoolean(false);
	
	private volatile Invoker<T> stickyInvoker = null;
	
	public AbstractClusterInvoker(Directory<T> directory) {
		this(directory, directory.getUrl());
	}
	
	public AbstractClusterInvoker(Directory<T> directory, URL url) {
		if (directory == null) {
			throw new IllegalArgumentException("service directory == null");
		}
		
		this.directory = directory;
		//sticky: invoker.isAvailable() should always be checked before using when availablecheck is true.
		this.availablecheck = url.getParameter(CLUSTER_AVAILABLE_CHECK_KEY, DEFAULT_CLUSTER_AVAILABLE_CHECK);
	}
	
	@Override
	public Class<T> getInterface() {
		return directory.getInterface();
	}
	
	@Override
	public URL getUrl() {
		return directory.getUrl();
	}
	
	@Override
	public boolean isAvailable() {
		Invoker<T> invoker = stickyInvoker;
		if (invoker != null) {
			return invoker.isAvailable();
		}
		return directory.isAvailable();
	}
	
	@Override
	public void destroy() {
		if (destroyed.compareAndSet(false, true)) {
			directory.destroy();
		}
	}
	
	/**
	 * Select a invoker using loadbalance policy.</br>
	 * a) Firstly, select an invoker using loadbalance. If this invoker is in previously selected list, or,
	 * if this invoker is unavailable, then continue step b (reselect), otherwise return the first selected invoker</br>
	 * <p>
	 * b) Reselection, the validation rule for reselection: selected > available. This rule guarantees that
	 * the selected invoker has the minimum chance to be one in the previously selected list, and also
	 * guarantees this invoker is available.
	 *
	 * @param loadbalance load balance policy
	 * @param invocation  invocation
	 * @param invokers    invoker candidates
	 * @param selected    exclude selected invokers or not
	 * @return the invoker which will final to do invoke.
	 * @throws RpcException exception
	 */
	protected Invoker<T> select(LoadBalance loadbalance, Invocation invocation,
	                            List<Invoker<T>> invokers, List<Invoker<T>> selected) throws RpcException {
		
		if (CollectionUtils.isEmpty(invokers)) {
			return null;
		}
		// 获取调用方法名
		String methodName = invocation == null ? StringUtils.EMPTY : invocation.getMethodName();
		// 获取 sticky 配置，sticky 表示粘滞连接。所谓粘滞连接是指让服务消费者尽可能的 调用同一个服务提供者，除非该提供者挂了再进行切换
		boolean sticky = invokers.get(0).getUrl()
				.getMethodParameter(methodName, CLUSTER_STICKY_KEY, DEFAULT_CLUSTER_STICKY);
		
		//ignore overloaded method
		if (stickyInvoker != null && !invokers.contains(stickyInvoker)) {
			stickyInvoker = null;
		}
		//ignore concurrency problem
		if (sticky && stickyInvoker != null && (selected == null || !selected.contains(stickyInvoker))) {
			if (availablecheck && stickyInvoker.isAvailable()) {
				return stickyInvoker;
			}
		}
		
		// 此时继续调用 doSelect 选择 Invoker
		Invoker<T> invoker = doSelect(loadbalance, invocation, invokers, selected);
		
		if (sticky) {
			stickyInvoker = invoker;
		}
		return invoker;
	}
	
	private Invoker<T> doSelect(LoadBalance loadbalance, Invocation invocation,
	                            List<Invoker<T>> invokers, List<Invoker<T>> selected) throws RpcException {
		
		if (CollectionUtils.isEmpty(invokers)) {
			return null;
		}
		if (invokers.size() == 1) {
			return invokers.get(0);
		}
		// 通过负载均衡组件选择 Invoker
		Invoker<T> invoker = loadbalance.select(invokers, getUrl(), invocation);
		
		//If the `invoker` is in the  `selected` or invoker is unavailable && availablecheck is true, reselect.
		if ((selected != null && selected.contains(invoker))
				|| (!invoker.isAvailable() && getUrl() != null && availablecheck)) {
			try {
				Invoker<T> rInvoker = reselect(loadbalance, invocation, invokers, selected, availablecheck);
				if (rInvoker != null) {
					invoker = rInvoker;
				} else {
					//Check the index of current selected invoker, if it's not the last one, choose the one at index+1.
					int index = invokers.indexOf(invoker);
					try {
						//Avoid collision
						invoker = invokers.get((index + 1) % invokers.size());
					} catch (Exception e) {
						logger.warn(e.getMessage() + " may because invokers list dynamic change, ignore.", e);
					}
				}
			} catch (Throwable t) {
				logger.error("cluster reselect fail reason is :" + t.getMessage() + " if can not solve, you can set cluster.availablecheck=false in url", t);
			}
		}
		return invoker;
	}
	
	/**
	 * Reselect, use invokers not in `selected` first, if all invokers are in `selected`,
	 * just pick an available one using loadbalance policy.
	 *
	 * @param loadbalance    load balance policy
	 * @param invocation     invocation
	 * @param invokers       invoker candidates
	 * @param selected       exclude selected invokers or not
	 * @param availablecheck check invoker available if true
	 * @return the reselect result to do invoke
	 * @throws RpcException exception
	 */
	private Invoker<T> reselect(LoadBalance loadbalance, Invocation invocation,
	                            List<Invoker<T>> invokers, List<Invoker<T>> selected, boolean availablecheck) throws RpcException {
		
		//Allocating one in advance, this list is certain to be used.
		List<Invoker<T>> reselectInvokers = new ArrayList<>(
				invokers.size() > 1 ? (invokers.size() - 1) : invokers.size());
		
		// First, try picking a invoker not in `selected`.
		for (Invoker<T> invoker : invokers) {
			if (availablecheck && !invoker.isAvailable()) {
				continue;
			}
			
			if (selected == null || !selected.contains(invoker)) {
				reselectInvokers.add(invoker);
			}
		}
		
		if (!reselectInvokers.isEmpty()) {
			return loadbalance.select(reselectInvokers, getUrl(), invocation);
		}
		
		// Just pick an available invoker using loadbalance policy
		if (selected != null) {
			for (Invoker<T> invoker : selected) {
				if ((invoker.isAvailable()) // available first
						&& !reselectInvokers.contains(invoker)) {
					reselectInvokers.add(invoker);
				}
			}
		}
		if (!reselectInvokers.isEmpty()) {
			return loadbalance.select(reselectInvokers, getUrl(), invocation);
		}
		
		return null;
	}
	
	@Override
	public Result invoke(final Invocation invocation) throws RpcException {
		checkWhetherDestroyed();
		
		// binding attachments into invocation.   绑定 attachments 到 invocation 中.
		Map<String, String> contextAttachments = RpcContext.getContext().getAttachments();
		if (contextAttachments != null && contextAttachments.size() != 0) {
			((RpcInvocation) invocation).addAttachments(contextAttachments);
		}
		// 从 RegistryDirectory 中获取 List<Invoker>     列举，检查Invoker 并进行路由
		List<Invoker<T>> invokers = list(invocation);
		// 加载 loadbalance 策略实现 默认加载的是 RandomLoadBalance 实现
		LoadBalance loadbalance = initLoadBalance(invokers, invocation);
		RpcUtils.attachInvocationIdIfAsync(getUrl(), invocation);
		// 调用 doInvoke 进行后续操作 抽象方法由各个子类去实现,默认  FailoverClusterInvoker
		return doInvoke(invocation, invokers, loadbalance);
	}
	
	protected void checkWhetherDestroyed() {
		if (destroyed.get()) {
			throw new RpcException("Rpc cluster invoker for " + getInterface() + " on consumer " + NetUtils.getLocalHost()
					+ " use dubbo version " + Version.getVersion()
					+ " is now destroyed! Can not invoke any more.");
		}
	}
	
	@Override
	public String toString() {
		return getInterface() + " -> " + getUrl().toString();
	}
	
	protected void checkInvokers(List<Invoker<T>> invokers, Invocation invocation) {
		if (CollectionUtils.isEmpty(invokers)) {
			throw new RpcException(RpcException.NO_INVOKER_AVAILABLE_AFTER_FILTER, "Failed to invoke the method "
					+ invocation.getMethodName() + " in the service " + getInterface().getName()
					+ ". No provider available for the service " + directory.getUrl().getServiceKey()
					+ " from registry " + directory.getUrl().getAddress()
					+ " on the consumer " + NetUtils.getLocalHost()
					+ " using the dubbo version " + Version.getVersion()
					+ ". Please check if the providers have been started and registered.");
		}
	}
	
	protected abstract Result doInvoke(Invocation invocation, List<Invoker<T>> invokers,
	                                   LoadBalance loadbalance) throws RpcException;
	
	protected List<Invoker<T>> list(Invocation invocation) throws RpcException {
		return directory.list(invocation);
	}
	
	/**
	 * Init LoadBalance.
	 * <p>
	 * if invokers is not empty, init from the first invoke's url and invocation
	 * if invokes is empty, init a default LoadBalance(RandomLoadBalance)
	 * </p>
	 *
	 * @param invokers   invokers
	 * @param invocation invocation
	 * @return LoadBalance instance. if not need init, return null.
	 */
	protected LoadBalance initLoadBalance(List<Invoker<T>> invokers, Invocation invocation) {
		if (CollectionUtils.isNotEmpty(invokers)) {
			return ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension(invokers.get(0).getUrl()
					.getMethodParameter(RpcUtils.getMethodName(invocation), LOADBALANCE_KEY, DEFAULT_LOADBALANCE));
		} else {
			return ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension(DEFAULT_LOADBALANCE);
		}
	}
}