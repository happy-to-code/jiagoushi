package com.itheima.rpc.netty.codec;

import com.itheima.rpc.data.RpcRequest;
import com.itheima.rpc.util.ProtostuffUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 二次编码
 * 将ByteBuf中的对象转换成 RPCRequest对象
 */
@Slf4j
public class RpcRequestDecoder extends MessageToMessageDecoder<ByteBuf> {
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> list) throws Exception {
		try {
			byte[] bytes = new byte[msg.readableBytes()];
			msg.readBytes(bytes); // 将ByteBuf中数据读到bytes中
			
			RpcRequest rpcRequest = ProtostuffUtil.deserialize(bytes, RpcRequest.class);
			list.add(rpcRequest);
		} catch (Exception e) {
			log.error("RpcRequestDecoder decode error,msg={}", e.getMessage());
			throw new RuntimeException(e);
		}
	}
}
