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
package org.apache.dubbo.remoting;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.Version;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.remoting.transport.ChannelHandlerAdapter;
import org.apache.dubbo.remoting.transport.ChannelHandlerDispatcher;

/**
 * Transporter facade. (API, Static, ThreadSafe)
 */
public class Transporters {
	
	static {
		// check duplicate jar package
		Version.checkDuplicate(Transporters.class);
		Version.checkDuplicate(RemotingException.class);
	}
	
	private Transporters() {
	}
	
	public static Server bind(String url, ChannelHandler... handler) throws RemotingException {
		return bind(URL.valueOf(url), handler);
	}
	
	public static Server bind(URL url, ChannelHandler... handlers) throws RemotingException {
		if (url == null) {
			throw new IllegalArgumentException("url == null");
		}
		if (handlers == null || handlers.length == 0) {
			throw new IllegalArgumentException("handlers == null");
		}
		ChannelHandler handler;
		if (handlers.length == 1) {
			handler = handlers[0];
		} else {
			// 如果 handlers 元素数量大于1，则创建 ChannelHandler 分发器
			handler = new ChannelHandlerDispatcher(handlers);
		}
		/**
		 * getTransporter()返回 Transporter的自适应实例, 并调用实例方法
		 * dubbo://192.168.200.10:20880/org.apache.dubbo.demo.DemoService?anyhost=true&application=demo-provider&bean.name=org.apache.dubbo.demo.DemoService&bind.ip=192.168.200.10&bind.port=20880&channel.readonly.sent=true&codec=dubbo&deprecated=false&dubbo=2.0.2&dynamic=true&generic=false&heartbeat=60000&interface=org.apache.dubbo.demo.DemoService&methods=sayHello&pid=12416&qos.port=22222&register=true&release=&side=provider&timestamp=1622540622168
		 *  最终找到的扩展点是: NettyTransporter
		 */
		return getTransporter().bind(url, handler);
	}
	
	public static Client connect(String url, ChannelHandler... handler) throws RemotingException {
		return connect(URL.valueOf(url), handler);
	}
	
	public static Client connect(URL url, ChannelHandler... handlers) throws RemotingException {
		if (url == null) {
			throw new IllegalArgumentException("url == null");
		}
		ChannelHandler handler;
		if (handlers == null || handlers.length == 0) {
			handler = new ChannelHandlerAdapter();
		} else if (handlers.length == 1) {
			handler = handlers[0];
		} else {
			// 如果 handler 数量大于1，则创建一个 ChannelHandler 分发器
			handler = new ChannelHandlerDispatcher(handlers);
		}
		// 获取 Transporter 自适应拓展类（默认   NettyTransporter ），并调用 connect 方法生成 Client 实例
		return getTransporter().connect(url, handler);
	}
	
	public static Transporter getTransporter() {
		return ExtensionLoader.getExtensionLoader(Transporter.class).getAdaptiveExtension();
	}
	
}