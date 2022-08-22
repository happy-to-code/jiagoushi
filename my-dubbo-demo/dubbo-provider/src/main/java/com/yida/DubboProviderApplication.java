package com.yida;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.yida"})
public class DubboProviderApplication {
	public static void main(String[] args) {
		SpringApplication.run(DubboProviderApplication.class, args);
	}
}
