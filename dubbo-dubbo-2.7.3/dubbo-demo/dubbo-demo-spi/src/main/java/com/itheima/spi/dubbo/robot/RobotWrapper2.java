package com.itheima.spi.dubbo.robot;

import com.itheima.spi.dubbo.Robot;
import org.apache.dubbo.rpc.Protocol;

public class RobotWrapper2 implements Robot {
	
	private Protocol protocol;
	
	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}
	
	
	private Robot robot;
	
	public RobotWrapper2(Robot robot) {
		this.robot = robot;
	}
	
	@Override
	public void sayHello() {
		System.out.println("----提前准备2----");
		robot.sayHello();
		System.out.println("wrapper inject2" + protocol);
		System.out.println("----收尾工作2----");
	}
}
