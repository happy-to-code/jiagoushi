package com.yida.dspi;

import org.apache.dubbo.common.extension.ExtensionLoader;

public class DubboIocSPIDemo {
	public static void main(String[] args) {
		ExtensionLoader<Phone> loader = ExtensionLoader.getExtensionLoader(Phone.class);
		Phone phone = loader.getExtension("iphone");
		phone.call();
	}
}
