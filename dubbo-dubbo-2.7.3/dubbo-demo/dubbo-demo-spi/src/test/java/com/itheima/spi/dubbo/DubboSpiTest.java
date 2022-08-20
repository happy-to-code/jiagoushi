package com.itheima.spi.dubbo;

import org.apache.dubbo.common.extension.ExtensionLoader;
import org.junit.Test;

/**
 * @description
 * @author: ts
 * @create:2021-05-27 16:57
 */
public class DubboSpiTest {
	
	// 测试 dubbo spi 机制
	@Test
	public void sayHello() throws Exception {
		//1、获得接口的 ExtentionLoader
		ExtensionLoader<Robot> extensionLoader = ExtensionLoader.getExtensionLoader(Robot.class);
		//2、根据指定的名字获(key)取对应的实例
        /* Robot robot = extensionLoader.getExtension("bumblebee");
        robot.sayHello();*/
		Robot optimusPrime = extensionLoader.getExtension("optimusPrime");
		//Robot adaptiveExtension = extensionLoader.getAdaptiveExtension();
		optimusPrime.sayHello();
		//Robot robot2 = extensionLoader.getDefaultExtension();
//        robot2.sayHello();


        /*ExtensionLoader<Filter> loader = ExtensionLoader.getExtensionLoader(Filter.class);
        List<Filter> activateExtension = loader.getActivateExtension(new URL("dubbo", "127.0.0.1", 8080), "protocol");
        for (Filter filter : activateExtension) {
            System.out.println(filter);
        }*/
	}
}
