package com.yida.dspi.impl;

import com.yida.dspi.Phone;

/**
 * 定义接口的实现类，也就是被装饰者
 */
public class Iphone implements Phone {
	@Override
	public void call() {
		System.out.println("iphone 正在打电话…………");
	}
}
