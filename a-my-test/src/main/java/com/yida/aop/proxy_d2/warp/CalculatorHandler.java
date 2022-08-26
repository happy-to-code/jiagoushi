package com.yida.aop.proxy_d2.warp;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author ：zhangyifei
 * @date ：Created in 2022/8/26 11:23
 * @description：
 * @modified By：
 * @version:
 */
public class CalculatorHandler<T> implements InvocationHandler {
	private T t;

	public CalculatorHandler(T t) {
		this.t = t;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.printf("日志信息：方法名:%s,参数:%s\n", method.getName(), Arrays.asList(args));

		// 调用原来方法
		Object resp = method.invoke(t, args[0], args[1]);

		return resp;
	}
}
