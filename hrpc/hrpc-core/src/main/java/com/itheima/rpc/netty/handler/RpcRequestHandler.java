package com.itheima.rpc.netty.handler;

import com.itheima.rpc.data.RpcRequest;
import com.itheima.rpc.data.RpcResponse;
import com.itheima.rpc.spring.SpringBeanFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

@Slf4j
@ChannelHandler.Sharable
public class RpcRequestHandler extends SimpleChannelInboundHandler<RpcRequest> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RpcRequest rpcRequest) throws Exception {
		// 有请求一定要有相应
		RpcResponse response = new RpcResponse();
		response.setRequestId(rpcRequest.getRequestId());
		
		try {
			// com.itheima.order.OrderService
			String serviceName = rpcRequest.getClassName();
			// 方法名称
			String methodName = rpcRequest.getMethodName();
			// 方法参数类型
			Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
			// 方法具体参数
			Object[] parameters = rpcRequest.getParameters();
			
			Object bean = SpringBeanFactory.getBean(Class.forName(serviceName));
			Method method = bean.getClass().getMethod(methodName, parameterTypes);
			Object result = method.invoke(bean, parameters);
			
			response.setResult(result);
		} catch (ClassNotFoundException e) {
			response.setCause(e);
			log.error("rpc server invoke error,msg={}", e.getMessage());
		} finally {
			ctx.channel().writeAndFlush(response);
		}
	}
	
	
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.error("服务端出现异常,异常信息为:{}", cause.getCause());
		ctx.fireExceptionCaught(cause);
	}
}
