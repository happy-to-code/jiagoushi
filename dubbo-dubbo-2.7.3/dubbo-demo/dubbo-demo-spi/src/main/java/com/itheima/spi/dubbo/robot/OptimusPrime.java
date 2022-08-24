package com.itheima.spi.dubbo.robot;

import com.itheima.spi.dubbo.PlatService;
import com.itheima.spi.dubbo.Robot;

public class OptimusPrime implements Robot {
	
	private PlatService platService;
	
	public void setPlatService(PlatService platService) {
		this.platService = platService;
	}
	
	
	@Override
	public void sayHello() {
		System.out.println("注入的platService" + platService);
		platService.printPlat();
		
		System.out.println("Hello, I am Optimus Prime.");
		//protocol.export(null);
	}
}
