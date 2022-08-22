package com.yida.dspi.impl;

import com.yida.dspi.Pay;
import org.apache.dubbo.common.URL;

public class AliPay implements Pay {
	
	@Override
	public void pay(URL url) {
		System.out.println("支付宝支付:" + url);
	}
}