package com.yida.dspi;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.SPI;

/**
 * adaptive
 */
@SPI("ali")
public interface Pay {
	
	@Adaptive({"payType"})
	void pay(URL url);
}
