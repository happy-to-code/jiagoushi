package com.itheima.spi.dubbo.robot;

import com.itheima.spi.dubbo.Robot;
import org.apache.dubbo.rpc.Protocol;

/**
 * @description
 * @author: ts
 * @create:2021-05-28 20:10
 */
public class RobotWrapper implements Robot {
	
	private Protocol protocol;
	
	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}
	
	
	private Robot robot;
	
	public RobotWrapper(Robot robot) {
		this.robot = robot;
	}
	
	@Override
	public void sayHello() {
		System.out.println("pre -----enchance ");
		robot.sayHello();
		System.out.println("wrapper inject" + protocol);
		System.out.println("----post enchance----");
	}
}
