package com.yida;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class DubboProviderApplication {
	public static void main(String[] args) {
		new EmbeddedZooKeeper(2181, false).start();
		
		SpringApplication.run(DubboProviderApplication.class, args);
	}
}
