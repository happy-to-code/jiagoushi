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
package org.apache.dubbo.rpc.proxy;

import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcInvocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * InvokerHandler
 */
public class InvokerInvocationHandler implements InvocationHandler {
	private static final Logger logger = LoggerFactory.getLogger(InvokerInvocationHandler.class);
	private final Invoker<?> invoker;
	
	
	public InvokerInvocationHandler(Invoker<?> handler) {
		this.invoker = handler;
	}
	
	/**
	 * 消费者方法调用会被此处拦截
	 *
	 * @param proxy
	 * @param method
	 * @param args
	 * @return
	 * @throws Throwable
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String methodName = method.getName();
		Class<?>[] parameterTypes = method.getParameterTypes();
		if (method.getDeclaringClass() == Object.class) {
			return method.invoke(invoker, args);
		}
		if ("toString".equals(methodName) && parameterTypes.length == 0) {
			return invoker.toString();
		}
		if ("hashCode".equals(methodName) && parameterTypes.length == 0) {
			return invoker.hashCode();
		}
		if ("equals".equals(methodName) && parameterTypes.length == 1) {
			return invoker.equals(args[0]);
		}
		/**
		 * Invocation 是会话域，它持有调用过程中的变量，比如方法名，参数等
		 * 将 method 和 args 封装到 RpcInvocation 中，并执行后续的调用
		 *
		 * invoker: MockClusterInvoker  内部封装了服务降级逻辑
		 */
		return invoker.invoke(new RpcInvocation(method, args)).recreate();
	}
}
