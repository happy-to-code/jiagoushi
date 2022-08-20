package com.itheima.spi.dubbo.plat;


import com.itheima.spi.dubbo.PlatService;

public class LinuxPlatService implements PlatService {
	@Override
	public void printPlat() {
		System.out.println("this is linux");
	}
	
	@Override
	public void doSelect(String plat) {
		System.out.println("Linux " + plat);
	}
}
