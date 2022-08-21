package com.yida;

import com.yida.service.HelloService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

@SpringBootApplication
@Service
@EnableDubbo
public class ConsumerApplication {
	
	@DubboReference
	private HelloService helloService;
	
	public static void main(String[] args) {
		
		ConfigurableApplicationContext context = SpringApplication.run(ConsumerApplication.class, args);
		ConsumerApplication application = context.getBean(ConsumerApplication.class);
		String result = application.doSayHello("小花");
		System.out.println("result: " + result);
	}
	
	public String doSayHello(String name) {
		return helloService.sayHello(name);
	}
}