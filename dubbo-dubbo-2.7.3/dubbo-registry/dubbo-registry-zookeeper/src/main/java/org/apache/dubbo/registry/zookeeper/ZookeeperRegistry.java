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
package org.apache.dubbo.registry.zookeeper;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.URLBuilder;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.common.utils.ConcurrentHashSet;
import org.apache.dubbo.common.utils.UrlUtils;
import org.apache.dubbo.registry.NotifyListener;
import org.apache.dubbo.registry.support.FailbackRegistry;
import org.apache.dubbo.remoting.Constants;
import org.apache.dubbo.remoting.zookeeper.ChildListener;
import org.apache.dubbo.remoting.zookeeper.StateListener;
import org.apache.dubbo.remoting.zookeeper.ZookeeperClient;
import org.apache.dubbo.remoting.zookeeper.ZookeeperTransporter;
import org.apache.dubbo.rpc.RpcException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.apache.dubbo.common.constants.CommonConstants.ANY_VALUE;
import static org.apache.dubbo.common.constants.CommonConstants.GROUP_KEY;
import static org.apache.dubbo.common.constants.CommonConstants.INTERFACE_KEY;
import static org.apache.dubbo.common.constants.CommonConstants.PATH_SEPARATOR;
import static org.apache.dubbo.common.constants.CommonConstants.PROTOCOL_SEPARATOR;
import static org.apache.dubbo.common.constants.RegistryConstants.CATEGORY_KEY;
import static org.apache.dubbo.common.constants.RegistryConstants.CONFIGURATORS_CATEGORY;
import static org.apache.dubbo.common.constants.RegistryConstants.CONSUMERS_CATEGORY;
import static org.apache.dubbo.common.constants.RegistryConstants.DEFAULT_CATEGORY;
import static org.apache.dubbo.common.constants.RegistryConstants.DYNAMIC_KEY;
import static org.apache.dubbo.common.constants.RegistryConstants.EMPTY_PROTOCOL;
import static org.apache.dubbo.common.constants.RegistryConstants.PROVIDERS_CATEGORY;
import static org.apache.dubbo.common.constants.RegistryConstants.ROUTERS_CATEGORY;

/**
 * ZookeeperRegistry
 */
public class ZookeeperRegistry extends FailbackRegistry {
	
	private final static Logger logger = LoggerFactory.getLogger(ZookeeperRegistry.class);
	
	private final static int DEFAULT_ZOOKEEPER_PORT = 2181;
	
	private final static String DEFAULT_ROOT = "dubbo";
	
	private final String root;
	
	private final Set<String> anyServices = new ConcurrentHashSet<>();
	
	private final ConcurrentMap<URL, ConcurrentMap<NotifyListener, ChildListener>> zkListeners = new ConcurrentHashMap<>();
	
	private final ZookeeperClient zkClient; // 实现是CuratorZookeeperClient
	
	public ZookeeperRegistry(URL url, ZookeeperTransporter zookeeperTransporter) { // ZookeeperTransporter
		super(url);
		if (url.isAnyHost()) {
			throw new IllegalStateException("registry address == null");
		}
		// 获取组名，默认为 dubbo
		String group = url.getParameter(GROUP_KEY, DEFAULT_ROOT);
		if (!group.startsWith(PATH_SEPARATOR)) {
			group = PATH_SEPARATOR + group;
		}
		this.root = group;
		// 创建 Zookeeper 客户端，默认为 CuratorZookeeperTransporter
		zkClient = zookeeperTransporter.connect(url);
		zkClient.addStateListener(state -> {
			if (state == StateListener.RECONNECTED) {
				try {
					recover();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		});
	}
	
	@Override
	public boolean isAvailable() {
		return zkClient.isConnected();
	}
	
	@Override
	public void destroy() {
		super.destroy();
		try {
			zkClient.close();
		} catch (Exception e) {
			logger.warn("Failed to close zookeeper client " + getUrl() + ", cause: " + e.getMessage(), e);
		}
	}
	
	@Override
	public void doRegister(URL url) {
		try {
			//url   dubbo://192.168.200.10:20880/org.apache.dubbo.demo.DemoService?anyhost=true&application=demo-provider&bean.name=org.apache.dubbo.demo.DemoService&deprecated=false&dubbo=2.0.2&dynamic=true&generic=false&interface=org.apache.dubbo.demo.DemoService&methods=sayHello&pid=10948&register=true&release=&side=provider&timestamp=1622545698657
			//path  /dubbo/org.apache.dubbo.demo.DemoService/providers/dubbo%3A%2F%2F192.168.200.10%3A20880%2Forg.apache.dubbo.demo.DemoService%3Fanyhost%3Dtrue%26application%3Ddemo-provider%26bean.name%3Dorg.apache.dubbo.demo.DemoService%26deprecated%3Dfalse%26dubbo%3D2.0.2%26dynamic%3Dtrue%26generic%3Dfalse%26interface%3Dorg.apache.dubbo.demo.DemoService%26methods%3DsayHello%26pid%3D17212%26register%3Dtrue%26release%3D%26side%3Dprovider%26timestamp%3D1622546575306
			String path = toUrlPath(url); //格式 /${group}/${serviceInterface}/providers/${url}
			zkClient.create(path, url.getParameter(DYNAMIC_KEY, true));
		} catch (Throwable e) {
			throw new RpcException("Failed to register " + url + " to zookeeper " + getUrl() + ", cause: " + e.getMessage(), e);
		}
	}
	
	@Override
	public void doUnregister(URL url) {
		try {
			zkClient.delete(toUrlPath(url));
		} catch (Throwable e) {
			throw new RpcException("Failed to unregister " + url + " to zookeeper " + getUrl() + ", cause: " + e.getMessage(), e);
		}
	}
	
	@Override
	public void doSubscribe(final URL url, final NotifyListener listener) {
		try {
			if (ANY_VALUE.equals(url.getServiceInterface())) {
				String root = toRootPath();
				ConcurrentMap<NotifyListener, ChildListener> listeners = zkListeners.get(url);
				if (listeners == null) {
					zkListeners.putIfAbsent(url, new ConcurrentHashMap<>());
					listeners = zkListeners.get(url);
				}
				ChildListener zkListener = listeners.get(listener);
				if (zkListener == null) {
					listeners.putIfAbsent(listener, (parentPath, currentChilds) -> {
						for (String child : currentChilds) {
							child = URL.decode(child);
							if (!anyServices.contains(child)) {
								anyServices.add(child);
								// 1
								subscribe(url.setPath(child).addParameters(INTERFACE_KEY, child,
										Constants.CHECK_KEY, String.valueOf(false)), listener);
							}
						}
					});
					zkListener = listeners.get(listener);
				}
				zkClient.create(root, false);//创建对应的节点信息
				List<String> services = zkClient.addChildListener(root, zkListener);
				if (CollectionUtils.isNotEmpty(services)) {
					for (String service : services) {
						service = URL.decode(service);
						anyServices.add(service);
						subscribe(url.setPath(service).addParameters(INTERFACE_KEY, service,
								Constants.CHECK_KEY, String.valueOf(false)), listener);
					}
				}
			} else {
				// 正常服务订阅
				List<URL> urls = new ArrayList<>();
				for (String path : toCategoriesPath(url)) {
					ConcurrentMap<NotifyListener, ChildListener> listeners = zkListeners.get(url);
					if (listeners == null) {
						zkListeners.putIfAbsent(url, new ConcurrentHashMap<>());
						listeners = zkListeners.get(url);
					}
					ChildListener zkListener = listeners.get(listener);
					if (zkListener == null) {
						listeners.putIfAbsent(listener, (parentPath, currentChilds) -> ZookeeperRegistry.this.notify(url, listener, toUrlsWithEmpty(url, parentPath, currentChilds)));
						zkListener = listeners.get(listener);
					}
					zkClient.create(path, false);
					List<String> children = zkClient.addChildListener(path, zkListener);
					if (children != null) {
						urls.addAll(toUrlsWithEmpty(url, path, children));
					}
				}
				//订阅节点后，要拉取节点的最新数据
				/**
				 * 最终会调用 RegistryDirectory 的 notify()方法
				 */
				notify(url, listener, urls);
			}
		} catch (Throwable e) {
			throw new RpcException("Failed to subscribe " + url + " to zookeeper " + getUrl() + ", cause: " + e.getMessage(), e);
		}
	}
	
	@Override
	public void doUnsubscribe(URL url, NotifyListener listener) {
		ConcurrentMap<NotifyListener, ChildListener> listeners = zkListeners.get(url);
		if (listeners != null) {
			ChildListener zkListener = listeners.get(listener);
			if (zkListener != null) {
				if (ANY_VALUE.equals(url.getServiceInterface())) {
					String root = toRootPath();
					zkClient.removeChildListener(root, zkListener);
				} else {
					for (String path : toCategoriesPath(url)) {
						zkClient.removeChildListener(path, zkListener);
					}
				}
			}
		}
	}
	
	@Override
	public List<URL> lookup(URL url) {
		if (url == null) {
			throw new IllegalArgumentException("lookup url == null");
		}
		try {
			List<String> providers = new ArrayList<>();
			for (String path : toCategoriesPath(url)) {
				List<String> children = zkClient.getChildren(path);
				if (children != null) {
					providers.addAll(children);
				}
			}
			return toUrlsWithoutEmpty(url, providers);
		} catch (Throwable e) {
			throw new RpcException("Failed to lookup " + url + " from zookeeper " + getUrl() + ", cause: " + e.getMessage(), e);
		}
	}
	
	private String toRootDir() {
		if (root.equals(PATH_SEPARATOR)) {
			return root;
		}
		return root + PATH_SEPARATOR;
	}
	
	private String toRootPath() {
		return root;
	}
	
	private String toServicePath(URL url) {
		String name = url.getServiceInterface();
		if (ANY_VALUE.equals(name)) {
			return toRootPath();
		}
		return toRootDir() + URL.encode(name);
	}
	
	private String[] toCategoriesPath(URL url) {
		String[] categories;
		if (ANY_VALUE.equals(url.getParameter(CATEGORY_KEY))) {
			categories = new String[]{PROVIDERS_CATEGORY, CONSUMERS_CATEGORY, ROUTERS_CATEGORY, CONFIGURATORS_CATEGORY};
		} else {
			categories = url.getParameter(CATEGORY_KEY, new String[]{DEFAULT_CATEGORY});
		}
		String[] paths = new String[categories.length];
		for (int i = 0; i < categories.length; i++) {
			paths[i] = toServicePath(url) + PATH_SEPARATOR + categories[i];
		}
		return paths;
	}
	
	private String toCategoryPath(URL url) {
		return toServicePath(url) + PATH_SEPARATOR + url.getParameter(CATEGORY_KEY, DEFAULT_CATEGORY);
	}
	
	private String toUrlPath(URL url) {
		return toCategoryPath(url) + PATH_SEPARATOR + URL.encode(url.toFullString());
	}
	
	private List<URL> toUrlsWithoutEmpty(URL consumer, List<String> providers) {
		List<URL> urls = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(providers)) {
			for (String provider : providers) {
				provider = URL.decode(provider);
				if (provider.contains(PROTOCOL_SEPARATOR)) {
					URL url = URL.valueOf(provider);
					if (UrlUtils.isMatch(consumer, url)) {
						urls.add(url);
					}
				}
			}
		}
		return urls;
	}
	
	private List<URL> toUrlsWithEmpty(URL consumer, String path, List<String> providers) {
		List<URL> urls = toUrlsWithoutEmpty(consumer, providers);
		if (urls == null || urls.isEmpty()) {
			int i = path.lastIndexOf(PATH_SEPARATOR);
			String category = i < 0 ? path : path.substring(i + 1);
			URL empty = URLBuilder.from(consumer)
					.setProtocol(EMPTY_PROTOCOL)
					.addParameter(CATEGORY_KEY, category)
					.build();
			urls.add(empty);
		}
		return urls;
	}
	
}