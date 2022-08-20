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
package org.apache.dubbo.demo.consumer;

import org.apache.dubbo.demo.DemoService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class Application {
	/**
	 * In order to make sure multicast registry works, need to specify '-Djava.net.preferIPv4Stack=true' before
	 * launch the application
	 */
	public static void main(String[] args) throws IOException {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/dubbo-consumer.xml");
		context.start();
		
		DemoService demoService = context.getBean("demoService", DemoService.class);
		// 执行服务调用时 将断点打到这查看生成的 demoService代理，查看代理中的 InvokerInvocationHandler
		String hello = demoService.sayHello("world");
		System.out.println("result: " + hello);
		
		System.in.read();
		
		//泛化调用
        /*GenericService genericService = context.getBean("genService",GenericService.class);
        Object $invoke = genericService.$invoke("sayHelloWord", new String[]{"java.lang.String"}, new Object[]{"world genericService"});
        System.out.println("泛化调用的结果为:"+$invoke);*/
		
	}
}