package netty.handler.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ServerInBoundHandler1 extends ChannelInboundHandlerAdapter {
	
	/**
	 * 通道准备就绪
	 *
	 * @param ctx
	 * @throws Exception
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		log.info("ServerInBoundHandler1 channelActive 通道准备就绪");
		super.channelActive(ctx);
	}
	
	/**
	 * 从通道中年读到了数据
	 *
	 * @param ctx
	 * @param msg
	 * @throws Exception
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// log.info("ServerInBoundHandler1 channelRead 从通道中年读到了数据,{}", ctx.channel().remoteAddress());
		// 将msg对象转换成ByteBuf
		ByteBuf buf = (ByteBuf) msg;
		byte[] bytes = new byte[buf.readableBytes()];
		buf.readBytes(bytes);
		String data = new String(bytes, Charset.defaultCharset());
		log.info("ServerInBoundHandler1 从通道中年读到了数据:{}", data);
		super.channelRead(ctx, msg);
	}
	
	/**
	 * 从通道中读取数据完成
	 *
	 * @param ctx
	 * @throws Exception
	 */
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		log.info("ServerInBoundHandler1 channelReadComplete 从通道中读取数据完成");
		// 向客户端写点儿数据
		ByteBuf buffer = ctx.alloc().buffer();
		buffer.writeBytes("hello NettyClient,i am NettyServer".getBytes(StandardCharsets.UTF_8));
		ctx.writeAndFlush(buffer);
		
		super.channelReadComplete(ctx);
	}
	
	/**
	 * 发生异常情况
	 *
	 * @param ctx
	 * @param cause
	 * @throws Exception
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.warn("发生异常");
		super.exceptionCaught(ctx, cause);
	}
}
