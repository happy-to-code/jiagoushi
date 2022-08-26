package com.yida.aop.proxy_d1.warp;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author ：zhangyifei
 * @date ：Created in 2022/8/26 11:02
 * @description：
 * @modified By：
 * @version:
 */
public class CarInvocationHandler<T> implements InvocationHandler {
	private T t;

	public CarInvocationHandler(T t) {
		this.t = t;
	}

	//使用反射对接口进行增强
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		for (Object a : args) {
			System.out.println("参数a = " + a);
		}
		// 调用原来的方法
		Object invokeReturn = method.invoke(t, args);

		// 增强方法
		System.out.println("增强方法 " + args[0] + "汽车还能使用电力");

		return invokeReturn;
	}
}
