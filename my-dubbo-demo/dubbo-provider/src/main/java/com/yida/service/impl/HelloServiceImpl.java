package com.yida.service.impl;

import com.yida.service.HelloService;

public class HelloServiceImpl implements HelloService {
	@Override
	public String sayHello(String name) {
		return name + ",你好！hello,world";
	}
}
