package com.itheima.spi.dubbo.robot;

import com.itheima.spi.dubbo.Robot;

public class Bumblebee implements Robot {
	
	@Override
	public void sayHello() {
		System.out.println("Hello, I am Bumblebee.");
	}
	
}
