package com.yida.dspi.impl;

import com.yida.dspi.Pay;
import org.apache.dubbo.common.URL;

public class WechatPay implements Pay {
	@Override
	public void pay(URL url) {
		System.out.println("微信支付:" + url);
	}
}