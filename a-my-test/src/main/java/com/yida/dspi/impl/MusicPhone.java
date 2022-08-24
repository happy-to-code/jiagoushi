package com.yida.dspi.impl;

import com.yida.dspi.Phone;

/**
 * 定义一个装饰者，实现phone接口，内部配置增强逻辑方法
 */
public class MusicPhone implements Phone {
	// 通过构造方法注入
	private Phone phone;
	
	public MusicPhone(Phone p) {
		this.phone = p;
	}
	
	@Override
	public void call() {
		System.out.println("彩铃播放:叮叮当、叮叮当、铃儿响叮当…………………………");
		this.phone.call();
	}
}
