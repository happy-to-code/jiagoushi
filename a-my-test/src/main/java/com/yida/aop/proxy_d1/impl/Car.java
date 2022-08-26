package com.yida.aop.proxy_d1.impl;

import com.yida.aop.proxy_d1.ICar;

/**
 * @author ：zhangyifei
 * @date ：Created in 2022/8/26 10:59
 * @description：
 * @modified By：
 * @version:
 */
public class Car implements ICar {
	@Override
	public String use(String name) {
		System.out.println(name + "--->汽车使用汽油");
		return name + " good car";
	}
}
