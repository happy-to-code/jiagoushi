package com.yida.handler.client;

import com.yida.pojo.UserInfo;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ClientInboundHandler1 extends ChannelInboundHandlerAdapter {
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		log.info("ClientInboundHandler1 channelActive-----");
		
		//批量发送数据
		UserInfo userInfo;
		for (int i = 0; i < 100; i++) {
			userInfo = new UserInfo(i, "name" + i, i + 1, (i % 2 == 0) ? "男" : "女", "北京");
			// ctx.writeAndFlush(ctx.alloc().buffer().writeBytes((userInfo.toString()+"\n").getBytes(StandardCharsets.UTF_8)));
			// ctx.writeAndFlush(ctx.alloc().buffer().writeBytes((userInfo.toString()).getBytes(StandardCharsets.UTF_8)));
			ctx.writeAndFlush(userInfo);
		}
		
		/*MessageProto.Message message;
		for (int i = 0; i < 100; i++) {
			message = MessageProto.Message.newBuilder().setId("message" + i).setContent("hello protobuf").build();
			ctx.writeAndFlush(message);
		}*/
		super.channelActive(ctx);
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		log.info("ClientInboundHandler1 channelRead-----");
		ByteBuf buf = (ByteBuf) msg;
		byte[] bytes = new byte[buf.readableBytes()];
		buf.readBytes(bytes);
		String data = new String(bytes, Charset.defaultCharset());
		log.info("ClientInboundHandler1:received client data = {}", data);
		super.channelRead(ctx, msg);
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		log.info("ClientInboundHandler1 channelReadComplete-----");
		super.channelReadComplete(ctx);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.info("ClientInboundHandler1 exceptionCaught-----");
		super.exceptionCaught(ctx, cause);
	}
}
