package com.itheima.dubbo.demo.consumer;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.demo.DemoService;

/**
 * @description
 * @author: ts
 * @create:2021-06-02 19:43
 */
public class BasicConsumer {
	
	
	public static void main(String[] args) {
		/**
		 * Application
		 * Reference
		 */
		ApplicationConfig applicationConfig = new ApplicationConfig();
		applicationConfig.setName("basic-consumer");
		
		ReferenceConfig referenceConfig = new ReferenceConfig();
		referenceConfig.setInterface(DemoService.class);
		referenceConfig.setUrl("dubbo://192.168.200.10:20880");
		referenceConfig.setApplication(applicationConfig);
		
		DemoService demoService = (DemoService) referenceConfig.get();
		String s = demoService.sayHello("唐僧老师");
		System.out.println(s);
	}
}
