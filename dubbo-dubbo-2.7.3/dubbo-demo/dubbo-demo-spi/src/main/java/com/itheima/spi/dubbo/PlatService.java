package com.itheima.spi.dubbo;

import org.apache.dubbo.common.extension.DisableInject;
import org.apache.dubbo.common.extension.SPI;

@SPI("windows")
public interface PlatService {
	void printPlat();
	
	//@Adaptive
	void doSelect(String plat);
}
