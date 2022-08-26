package com.yida.aop.proxy_d1;

import com.yida.aop.proxy_d1.impl.Car;
import com.yida.aop.proxy_d1.warp.CarInvocationHandler;

import java.lang.reflect.Proxy;

/**
 * @author ：zhangyifei
 * @date ：Created in 2022/8/26 11:06
 * @description：
 * @modified By：
 * @version:
 */
public class Demo {
	public static void main(String[] args) {
		ICar car = new Car();

		CarInvocationHandler carInvocationHandler = new CarInvocationHandler(car);

		Object proxy = Proxy.newProxyInstance(car.getClass().getClassLoader(), car.getClass().getInterfaces(), carInvocationHandler);

		if (proxy instanceof ICar) {
			ICar iCar = (ICar) proxy;
			String use = iCar.use("比亚迪");
			System.out.println("调用方法返回值 = " + use);
		}
	}
}
