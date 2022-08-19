package com.itheima.rpc.netty.codec;

import com.itheima.rpc.data.RpcResponse;
import com.itheima.rpc.util.ProtostuffUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 二次编码：将对象编码成 buffer
 */
@Slf4j
public class RpcResponseEncoder extends MessageToMessageEncoder<RpcResponse> {
	// 将RpcResponse对象编码成Buffer，放到List<Object> out 数组中然后返回出去
	@Override
	protected void encode(ChannelHandlerContext ctx, RpcResponse rpcResponse, List<Object> out) throws Exception {
		try {
			byte[] bytes = ProtostuffUtil.serialize(rpcResponse);
			
			ByteBuf buf = ctx.alloc().buffer(bytes.length);
			buf.writeBytes(bytes);
			
			out.add(buf);
		} catch (Exception e) {
			log.error("RpcResponseEncoder encode error,msg={}", e.getMessage());
			throw new RuntimeException(e);
		}
	}
}
