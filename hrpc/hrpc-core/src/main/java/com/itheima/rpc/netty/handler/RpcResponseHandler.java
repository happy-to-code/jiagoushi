package com.itheima.rpc.netty.handler;

import com.itheima.rpc.data.RpcResponse;
import com.itheima.rpc.netty.request.RequestPromise;
import com.itheima.rpc.netty.request.RpcRequestHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcResponseHandler extends SimpleChannelInboundHandler<RpcResponse> {
	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse rpcResponse) throws Exception {
		RequestPromise requestPromise = RpcRequestHolder.getRequestPromise(rpcResponse.getRequestId());
		if (requestPromise != null) {
			requestPromise.setSuccess(rpcResponse);
		}
		
	}
}
