package com.yida.dspi;

import org.apache.dubbo.common.extension.SPI;

/**
 * dubbo  SPI
 */
@SPI
public interface Robot {
	void dubboSayHello();
}
