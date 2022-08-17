package com.itheima.rpc.server.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class RpcServerBootstrap {
	
	@Autowired
	private RpcServerRunner rpcServerRunner;
	
	// @PostConstruct这个注解是由Java提供的，它用来修饰一个非静态的void方法。
	// 它会在服务器加载Servlet的时候运行，并且只运行一次。
	@PostConstruct
	public void startServer() {
		rpcServerRunner.run();
	}
}
