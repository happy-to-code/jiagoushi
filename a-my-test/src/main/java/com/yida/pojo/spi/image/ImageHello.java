package com.yida.pojo.spi.image;

import com.yida.pojo.spi.HelloSPI;

public class ImageHello implements HelloSPI {
	@Override
	public void sayHello() {
		System.out.println("================image sayHello================");
	}
}
