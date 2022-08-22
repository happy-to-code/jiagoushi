package com.yida.spi.image;

import com.yida.spi.HelloSPI;

public class ImageHello implements HelloSPI {
	@Override
	public void sayHello() {
		System.out.println("================image sayHello================");
	}
}
