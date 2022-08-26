package com.yida.aop.proxy_d2.impl;

import com.yida.aop.proxy_d2.ICalculator;

/**
 * @author ：zhangyifei
 * @date ：Created in 2022/8/26 11:20
 * @description：
 * @modified By：
 * @version:
 */
// @Service
public class Calculator implements ICalculator {
	@Override
	public int add(int num1, int num2) {
		return num1 + num2;
	}

	@Override
	public int subtract(int num1, int num2) {
		return num1 - num2;
	}

	@Override
	public int multiply(int num1, int num2) {
		return num1 * num2;
	}

	@Override
	public int divide(int num1, int num2) {
		return num1 / num2;
	}
}
