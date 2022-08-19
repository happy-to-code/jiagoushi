package com.itheima.rpc.client.request;

import com.itheima.rpc.cache.ServiceProviderCache;
import com.itheima.rpc.client.cluster.LoadBalanceStrategy;
import com.itheima.rpc.client.cluster.StartegyProvider;
import com.itheima.rpc.data.RpcRequest;
import com.itheima.rpc.data.RpcResponse;
import com.itheima.rpc.enums.StatusEnum;
import com.itheima.rpc.exception.RpcException;
import com.itheima.rpc.netty.codec.FrameDecoder;
import com.itheima.rpc.netty.codec.FrameEncoder;
import com.itheima.rpc.netty.codec.RpcRequestEncoder;
import com.itheima.rpc.netty.codec.RpcResponseDecoder;
import com.itheima.rpc.netty.handler.RpcResponseHandler;
import com.itheima.rpc.netty.request.ChannelMapping;
import com.itheima.rpc.netty.request.RequestPromise;
import com.itheima.rpc.netty.request.RpcRequestHolder;
import com.itheima.rpc.provider.ServiceProvider;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.List;

@Slf4j
@Component
public class RpcRequestManager {
	@Autowired
	private ServiceProviderCache serviceProviderCache;  // 缓存服务
	
	@Autowired
	private StartegyProvider startegyProvider;
	
	
	/**
	 * 向服务端发送请求并获取响应
	 *
	 * @param request
	 * @return
	 */
	public RpcResponse sendRequest(RpcRequest request) {
		// com.itheima.shop.order.OrderService
		//  从缓存中获取接口提供者服务信息
		List<ServiceProvider> serviceProviders = serviceProviderCache.get(request.getClassName());
		System.out.println("serviceProviders = " + serviceProviders);
		if (serviceProviders.size() == 0) {
			log.error("客户端没有发现服务端节点信息");
			throw new RpcException(StatusEnum.NOT_FOUND_SERVICE_PROVINDER);
		}
		// 获取服务端的一个节点
		// ServiceProvider serviceProvider = serviceProviders.get(0);
		
		LoadBalanceStrategy loadBalanceStrategy = startegyProvider.getStrategy();
		ServiceProvider serviceProvider = loadBalanceStrategy.select(serviceProviders);
		if (serviceProvider == null) {
			throw new RuntimeException("没有发现服务端停供的服务");
		}
		
		// 发起netty网络调用
		return requestByNetty(request, serviceProvider);
	}
	
	/**
	 * netty 发起远程调用
	 *
	 * @param request
	 * @param serviceProvider
	 * @return
	 */
	private RpcResponse requestByNetty(RpcRequest request, ServiceProvider serviceProvider) {
		try {
			Channel channel = null; // Netty中的Channel  不要导成nio的channel
			String serverIp = serviceProvider.getServerIp(); // 远程服务器的ip
			int rpcPort = serviceProvider.getRpcPort();// 远程服务端端口
			//	判断是否已经存在channel，没有再创建爱你
			if (!RpcRequestHolder.channelExist(serverIp, rpcPort)) { // 不存在==》创建
				EventLoopGroup group = new NioEventLoopGroup(0, new DefaultThreadFactory("client"));
				// 客户端引导类
				Bootstrap bootstrap = new Bootstrap();
				bootstrap.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel sc) throws Exception {
						ChannelPipeline pipeline = sc.pipeline();
						//	========================^===========================
						//	========================|===========================
						//	========================|===========================
						//	=======================编码==========================
						pipeline.addLast("frameEncoder", new FrameEncoder());
						pipeline.addLast("rpcRequestEncoder", new RpcRequestEncoder());
						//	-------------------------------------------------------------------------------
						pipeline.addLast("frameDecoder", new FrameDecoder());
						pipeline.addLast("rpcResponseDecoder", new RpcResponseDecoder());
						//	=======================解码==========================
						//	========================|============================
						//	========================|============================
						//	========================v============================
						pipeline.addLast("rpcResponseHandler", new RpcResponseHandler());
					}
				});
				
				//	向服务端发送请求   上面的操作是回调操作
				//  和服务端建立连接
				ChannelFuture future = bootstrap.connect(serverIp, rpcPort).sync();
				//	异步操作  注册监听
				if (future.isSuccess()) {
					channel = future.channel();
					
					//	增加映射
					ChannelMapping channelMapping = new ChannelMapping(serverIp, rpcPort, channel);
					RpcRequestHolder.addChannelMapping(channelMapping);
				}
			} else {
				channel = RpcRequestHolder.getChannel(serverIp, rpcPort);
			}
			
			// 注册回调  promise  future
			RequestPromise requestPromise = new RequestPromise(channel.eventLoop());
			
			RpcRequestHolder.addRequestPromise(request.getRequestId(), requestPromise);
			
			//	向服务端发送数据
			channel.writeAndFlush(request);
			
			//	等待服务端响应
			RpcResponse response = (RpcResponse) requestPromise.get();
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			RpcRequestHolder.removeRequestPromise(request.getRequestId());
		}
		
		// 返回一个空的 响应
		return new RpcResponse();
	}
}
