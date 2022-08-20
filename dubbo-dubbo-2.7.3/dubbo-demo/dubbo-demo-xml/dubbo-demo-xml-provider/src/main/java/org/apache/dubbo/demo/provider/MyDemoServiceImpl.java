package org.apache.dubbo.demo.provider;

import org.apache.dubbo.demo.MyDemoService;

/**
 * @description
 * @author: ts
 * @create:2021-06-08 09:57
 */
public class MyDemoServiceImpl implements MyDemoService {
	@Override
	public String sayHelloWord(String name) {
		return "你好啊," + name;
	}
}
