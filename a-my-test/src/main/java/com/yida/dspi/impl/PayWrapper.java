package com.yida.dspi.impl;

import com.yida.dspi.Pay;
import org.apache.dubbo.common.URL;

public class PayWrapper implements Pay {
	
	private Pay pay;
	
	public PayWrapper(Pay p) {
		this.pay = p;
	}
	
	@Override
	public void pay(URL url) {
		System.out.println("----支付前校验----");
		pay.pay(url);
		System.out.println("---->>>通知发货----");
	}
}
