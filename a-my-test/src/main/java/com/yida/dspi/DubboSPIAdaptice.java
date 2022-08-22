package com.yida.dspi;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.ExtensionLoader;

public class DubboSPIAdaptice {
	public static void main(String[] args) {
		ExtensionLoader<Pay> extensionLoader = ExtensionLoader.getExtensionLoader(Pay.class);
		Pay adaptiveExtension = extensionLoader.getAdaptiveExtension();
		
		adaptiveExtension.pay(URL.valueOf("dubbo://192.168.0.101:20880/xxxService?payType=ali"));
		System.out.println("---------------");
		adaptiveExtension.pay(URL.valueOf("dubbo://192.168.0.101:20880/xxxService?payType=wechat"));
	}
}
