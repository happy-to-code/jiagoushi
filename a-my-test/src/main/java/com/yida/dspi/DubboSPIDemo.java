package com.yida.dspi;

import org.apache.dubbo.common.extension.ExtensionLoader;

public class DubboSPIDemo {
	public static void main(String[] args) {
		// 通过ExtensionLoader获取加载器
		ExtensionLoader<Robot> extensionLoader = ExtensionLoader.getExtensionLoader(Robot.class);
		// 通过加载器获取指定实现类
		Robot bumblebee = extensionLoader.getExtension("bumblebee");
		bumblebee.dubboSayHello();
		System.out.println("---------------");
		Robot optimusPrime = extensionLoader.getExtension("optimusPrime");
		optimusPrime.dubboSayHello();
	}
}
