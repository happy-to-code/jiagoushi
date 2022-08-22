package com.yida.spi.text;

import com.yida.spi.HelloSPI;

public class TextHello implements HelloSPI {
	@Override
	public void sayHello() {
		System.out.println("----------------text sayHello------------------");
	}
}
