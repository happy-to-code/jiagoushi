package com.itheima.rpc.server.registry;

public interface RpcRegistry {
	/**
	 * 服务注册
	 * 向zk  nacos eruik等注册中心注册
	 */
	void serviceRegistry();
}
