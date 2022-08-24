package com.itheima.spi.dubbo;


import org.apache.dubbo.common.extension.SPI;

//@SPI("bumblebee") //bumblebee表示默认加载该对象实例
@SPI
public interface Robot {
	
	//@Adaptive
	void sayHello();
}
