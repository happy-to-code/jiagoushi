package com.yida.order.mq;

import org.springframework.beans.factory.annotation.Value;

//@EnableBinding(Sink.class)
public class MessageReceiver {
	
	@Value("${server.port}")
	private String port;
	
	/****
	 * 消息监听
	 * @param message
	 */
	//@StreamListener(Sink.INPUT)
	public void receive(String message) {
		System.out.println("消息监听(增加用户积分、修改订单状态)-->" + message + "-->port:" + port);
	}
}

