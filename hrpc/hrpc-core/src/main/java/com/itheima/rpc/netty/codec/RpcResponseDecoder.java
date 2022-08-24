package com.itheima.rpc.netty.codec;

import com.itheima.rpc.data.RpcResponse;
import com.itheima.rpc.util.ProtostuffUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 二次解码  转换成对象
 */
@Slf4j
public class RpcResponseDecoder extends MessageToMessageDecoder<ByteBuf> {
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
		try {
			//	从byteBuf中获取数据
			byte[] bytes = new byte[byteBuf.readableBytes()];
			byteBuf.readBytes(bytes);
			
			RpcResponse rpcResponse = ProtostuffUtil.deserialize(bytes, RpcResponse.class);
			list.add(rpcResponse);
		} catch (Exception e) {
			throw new RuntimeException("RpcResponseDecoder 二次解码失败：" + e.getMessage());
		}
	}
}
