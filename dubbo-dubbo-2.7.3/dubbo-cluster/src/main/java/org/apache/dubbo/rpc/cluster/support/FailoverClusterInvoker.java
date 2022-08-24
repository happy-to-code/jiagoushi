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

import org.apache.dubbo.common.Version;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.common.utils.NetUtils;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.Directory;
import org.apache.dubbo.rpc.cluster.LoadBalance;
import org.apache.dubbo.rpc.support.RpcUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.dubbo.rpc.cluster.Constants.DEFAULT_RETRIES;
import static org.apache.dubbo.rpc.cluster.Constants.RETRIES_KEY;

/**
 * When invoke fails, log the initial error and retry other invokers (retry n times, which means at most n different invokers will be invoked)
 * Note that retry causes latency.
 * <p>
 * <a href="http://en.wikipedia.org/wiki/Failover">Failover</a>
 */
public class FailoverClusterInvoker<T> extends AbstractClusterInvoker<T> {
	
	private static final Logger logger = LoggerFactory.getLogger(FailoverClusterInvoker.class);
	
	public FailoverClusterInvoker(Directory<T> directory) {
		super(directory);
	}
	
	@Override
	@SuppressWarnings({"unchecked", "rawtypes"})
	public Result doInvoke(Invocation invocation, final List<Invoker<T>> invokers, LoadBalance loadbalance) throws RpcException {
		// 每一个服务提供者都会对应一个Invoker对象
		List<Invoker<T>> copyInvokers = invokers;
		checkInvokers(copyInvokers, invocation);
		String methodName = RpcUtils.getMethodName(invocation);
		// 重试次数+1=总调用次数
		int len = getUrl().getMethodParameter(methodName, RETRIES_KEY, DEFAULT_RETRIES) + 1;
		if (len <= 0) {
			len = 1;
		}
		// retry loop.
		RpcException le = null; // last exception.
		List<Invoker<T>> invoked = new ArrayList<Invoker<T>>(copyInvokers.size()); // invoked invokers.
		Set<String> providers = new HashSet<String>(len);
		// 循环调用开始
		for (int i = 0; i < len; i++) {
			//Reselect before retry to avoid a change of candidate `invokers`.
			//NOTE: if `invokers` changed, then `invoked` also lose accuracy.
			if (i > 0) {
				/**
				 * i>0 时在进行重试前检查并重新列举 Invoker，这样做的好处是，如果某个服务挂了，
				 * 通过调用 list 可得到最新可用的 Invoker 列表 同时在list中也会重新路由
				 */
				checkWhetherDestroyed();
				copyInvokers = list(invocation);
				// check again 对 copyinvokers 进行判空检查
				checkInvokers(copyInvokers, invocation);
			}
			// 执行 loadbalance   通过负载均衡选择 Invoker
			Invoker<T> invoker = select(loadbalance, invocation, copyInvokers, invoked);
			invoked.add(invoker);
			// 设置 invoked 到 RPC 上下文中
			RpcContext.getContext().setInvokers((List) invoked);
			try {
				//调用目标 Invoker 的 invoke 方法  这里的 invoker= RegistryDirectory$InvokerDelegate--->中间走一系列 Filter,走 AsyncToSyncInvoker 的invoke方法 最终走到 DubboInvoker
				Result result = invoker.invoke(invocation);
				if (le != null && logger.isWarnEnabled()) {
					logger.warn("Although retry the method " + methodName
							+ " in the service " + getInterface().getName()
							+ " was successful by the provider " + invoker.getUrl().getAddress()
							+ ", but there have been failed providers " + providers
							+ " (" + providers.size() + "/" + copyInvokers.size()
							+ ") from the registry " + directory.getUrl().getAddress()
							+ " on the consumer " + NetUtils.getLocalHost()
							+ " using the dubbo version " + Version.getVersion() + ". Last error is: "
							+ le.getMessage(), le);
				}
				return result;
			} catch (RpcException e) {
				if (e.isBiz()) { // biz exception.
					throw e;
				}
				le = e;
			} catch (Throwable e) {
				le = new RpcException(e.getMessage(), e);
			} finally {
				providers.add(invoker.getUrl().getAddress());
			}
		}
		throw new RpcException(le.getCode(), "Failed to invoke the method "
				+ methodName + " in the service " + getInterface().getName()
				+ ". Tried " + len + " times of the providers " + providers
				+ " (" + providers.size() + "/" + copyInvokers.size()
				+ ") from the registry " + directory.getUrl().getAddress()
				+ " on the consumer " + NetUtils.getLocalHost() + " using the dubbo version "
				+ Version.getVersion() + ". Last error is: "
				+ le.getMessage(), le.getCause() != null ? le.getCause() : le);
	}
	
}
