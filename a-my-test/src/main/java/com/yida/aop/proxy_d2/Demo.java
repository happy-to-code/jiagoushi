package com.yida.aop.proxy_d2;

import com.yida.aop.proxy_d2.impl.Calculator;
import com.yida.aop.proxy_d2.warp.CalculatorHandler;

import java.lang.reflect.Proxy;

/**
 * @author ：zhangyifei
 * @date ：Created in 2022/8/26 13:22
 * @description：
 * @modified By：
 * @version:
 */
public class Demo {
	public static void main(String[] args) {
		ICalculator iCalculator = new Calculator();

		CalculatorHandler handler = new CalculatorHandler(iCalculator);

		ICalculator iCalculatorProxyInstance = (ICalculator) Proxy.newProxyInstance(iCalculator.getClass().getClassLoader(),
				iCalculator.getClass().getInterfaces(), handler);

		Integer num1 = 10;
		Integer num2 = 5;

		int result = iCalculatorProxyInstance.add(num1, num2);
		System.out.println("add result = " + result);

		result = iCalculatorProxyInstance.subtract(num1, num2);
		System.out.println("sub result = " + result);

		result = iCalculatorProxyInstance.multiply(num1, num2);
		System.out.println("mul result = " + result);

		result = iCalculatorProxyInstance.divide(num1, num2);
		System.out.println("divide result = " + result);
	}
}
