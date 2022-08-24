package com.yida.dspi;

import org.apache.dubbo.common.extension.SPI;

@SPI
public interface Phone {
	void call();
}
