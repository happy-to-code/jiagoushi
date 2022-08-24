package com.itheima.rpc.netty.codec;

import com.itheima.rpc.data.RpcRequest;
import com.itheima.rpc.exception.RpcException;
import com.itheima.rpc.util.ProtostuffUtil;
import com.itheima.rpc.util.RequestIdUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 二次编码
 * 将对象编码成二进制文件 然后写到buffer中
 */
@Slf4j
public class RpcRequestEncoder extends MessageToMessageEncoder<RpcRequest> {
	@Override
	protected void encode(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest, List<Object> list) throws Exception {
		try {
			byte[] bytes = ProtostuffUtil.serialize(rpcRequest); // 编码
			
			// 写入buffer
			ByteBuf buffer = channelHandlerContext.alloc().buffer(bytes.length);
			buffer.writeBytes(bytes);
			
			list.add(buffer);
		} catch (Exception e) {
			throw new RpcException("RpcRequestEncoder二次编码出错：" + e.getMessage());
		}
	}
}
