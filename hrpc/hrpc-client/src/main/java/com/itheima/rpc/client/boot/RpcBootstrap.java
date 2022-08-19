package com.itheima.rpc.client.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RpcBootstrap {
	@Autowired
	private RpcClientRunner rpcClientRunner;
	
	@PostConstruct
	public void initRpcClient() {
		System.out.println("--------------------initRpcClient-------------------------");
		rpcClientRunner.run();
	}
	
}
