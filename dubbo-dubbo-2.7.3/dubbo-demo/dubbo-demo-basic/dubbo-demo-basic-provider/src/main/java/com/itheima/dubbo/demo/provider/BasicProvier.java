package com.itheima.dubbo.demo.provider;

import org.apache.dubbo.config.*;
import org.apache.dubbo.demo.DemoService;

import java.io.IOException;

/**
 * @description
 * @author: ts
 * @create:2021-06-02 19:42
 */
public class BasicProvier {
	
	public static void main(String[] args) throws IOException {
		
		/**
		 * Application
		 * Protocol
		 * Registry
		 * DubboService
		 */
		ApplicationConfig applicationConfig = new ApplicationConfig();
		applicationConfig.setName("basic-provider");
		
		ProtocolConfig protocolConfig = new ProtocolConfig();
		protocolConfig.setName("dubbo");
		protocolConfig.setPort(-1);//默认20880
		
		RegistryConfig registryConfig = new RegistryConfig();
		registryConfig.setAddress(RegistryConfig.NO_AVAILABLE);//不需要注册中心
		
		ServiceConfig serviceConfig = new ServiceConfig();
		serviceConfig.setInterface(DemoService.class);
		serviceConfig.setRef(new DemoServiceImpl());
		
		serviceConfig.setApplication(applicationConfig);
		serviceConfig.setProtocol(protocolConfig);
		serviceConfig.setRegistry(registryConfig);
		//服务导出(暴露)
		serviceConfig.export();
		
		System.in.read();
	}
}
