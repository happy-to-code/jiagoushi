package com.yida.pojo.spi;

import java.util.ServiceLoader;

public class SPIDemo {
	public static void main(String[] args) {
		ServiceLoader<HelloSPI> serviceLoader = ServiceLoader.load(HelloSPI.class);
		for (HelloSPI helloSPI : serviceLoader) {
			helloSPI.sayHello();
		}
	}
}
