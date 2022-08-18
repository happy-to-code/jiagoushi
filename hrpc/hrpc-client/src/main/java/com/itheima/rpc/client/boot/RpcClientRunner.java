package com.itheima.rpc.client.boot;

import com.itheima.rpc.client.discovery.RpcServiceDiscovery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RpcClientRunner {
	/**
	 * 客户端完成服务发现（从zk配置中心拉取消息--->注册一个监听服务，监听zk节点的变化）
	 * 1、从zk中拉取服务信息，存入缓存
	 * 2、扫描注解 @HrpcRemote 注解，为标记这个注解的接口生成代理并注入
	 * 3、当代理=对象走到代理拦截面时，然后就远程调用Netty服务端执行真正的接口，然后获取响应
	 */
	
	@Autowired
	private RpcServiceDiscovery serviceDiscovery;
	
	public void run() {
		serviceDiscovery.serviceDiscovery();
	}
	
}
