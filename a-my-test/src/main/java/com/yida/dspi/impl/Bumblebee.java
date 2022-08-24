package com.yida.dspi.impl;
import com.yida.dspi.Robot;

/**
 * 大黄蜂
 */
public class Bumblebee implements Robot {
	@Override
	public void dubboSayHello() {
		System.out.println("Hello, I am 大黄蜂.");
	}
}
