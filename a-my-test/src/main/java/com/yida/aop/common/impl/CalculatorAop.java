package com.yida.aop.common.impl;

import com.yida.aop.common.ICalculatorAop;
import org.springframework.stereotype.Service;

/**
 * @author ：zhangyifei
 * @date ：Created in 2022/8/26 11:20
 * @description：
 * @modified By：
 * @version:
 */
@Service
public class CalculatorAop implements ICalculatorAop {
	@Override
	public int add(int num1, int num2) {
		System.out.println("========>add方法执行中…………num1 = " + num1 + ", num2 = " + num2);
		return num1 + num2;
	}

	@Override
	public int subtract(int num1, int num2) {
		System.out.println("========>subtract法执行中…………num1 = " + num1 + ", num2 = " + num2);
		return num1 - num2;
	}

	@Override
	public int multiply(int num1, int num2) {
		System.out.println("========>multiply法执行中…………num1 = " + num1 + ", num2 = " + num2);
		return num1 * num2;
	}

	@Override
	public int divide(int num1, int num2) {
		System.out.println("========>divide法执行中…………num1 = " + num1 + ", num2 = " + num2);
		return num1 / num2;
	}
}
