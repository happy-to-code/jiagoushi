package com.itheima.rpc.server.boot;


import com.itheima.rpc.server.registry.RpcRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RpcServerRunner {
	
	@Autowired
	private RpcRegistry rpcRegistry;
	
	@Autowired
	private RpcServer rpcServer;
	
	public void run() {
		/**
		 * rpc server端需要完成的任务
		 * 1、服务信息注册到服务注册中心
		 *      1.1、扫描业务代码中的接口，扫描完成将接口信息写到注册中心
		 *      @see com.itheima.rpc.annotation.HrpcService
		 * 2、基于Netty编写一个服务端
		 *      2.1、分析服务端需要完成的Handler，完成什么样的功能
		 *      2.2、入站的一次、二次解码器
		 *      2.2、请求处理器Handler：根据参数调用某个真实的接口方法
		 *      2.3、出站的一次、二次遍码器
		 */
		// 服务端信息注册到  服务注册中心---> zookeeper
		rpcRegistry.serviceRegistry();
		
		// 启动Netty服务器  该服务器里封装了编解码、调用接口真实方法
		rpcServer.start();
	}
	
}
