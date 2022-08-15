package com.yida.handler.server;

import com.yida.pojo.UserInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TcpStickHalfHandler1 extends ChannelInboundHandlerAdapter {
	int count = 0;
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// MessageProto.Message data = (MessageProto.Message) msg;
		UserInfo data = (UserInfo) msg;
		count++;
		log.info("---服务端收到的第{}个数据:{}", count, data);
	}
}