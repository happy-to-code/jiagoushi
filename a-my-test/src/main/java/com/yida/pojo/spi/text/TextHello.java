package com.yida.pojo.spi.text;

import com.yida.pojo.spi.HelloSPI;

public class TextHello implements HelloSPI {
	@Override
	public void sayHello() {
		System.out.println("----------------text sayHello------------------");
	}
}
