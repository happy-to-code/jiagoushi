package com.itheima.spi.dubbo.plat;


import com.itheima.spi.dubbo.PlatService;

public class WindowsPlatService implements PlatService {
	@Override
	public void printPlat() {
		System.out.println("this is windows");
	}
	
	@Override
	public void doSelect(String plat) {
		System.out.println("windows " + plat);
	}
}
