package com.itheima.rpc.server.boot.nett;

import com.itheima.rpc.server.boot.RpcServer;
import com.itheima.rpc.server.config.RpcServerConfiguration;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.NettyRuntime;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.UnorderedThreadPoolEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NettyServer implements RpcServer {
	// 配置文件
	@Autowired
	private RpcServerConfiguration serverConfiguration;
	
	/**
	 * * 2、基于Netty编写一个服务端
	 * *      2.1、分析服务端需要完成的Handler，完成什么样的功能
	 * *      2.2、入站的一次、二次解码器
	 * *      2.2、请求处理器Handler：根据参数调用某个真实的接口方法
	 * *      2.3、出站的一次、二次遍码器
	 */
	@Override
	public void start() {
		EventLoopGroup boss = new NioEventLoopGroup(1, new DefaultThreadFactory("boss"));
		EventLoopGroup worker = new NioEventLoopGroup(0, new DefaultThreadFactory("worker"));
		EventExecutorGroup business = new UnorderedThreadPoolEventExecutor((NettyRuntime.availableProcessors() * 2) + 1,
				new DefaultThreadFactory("business"));
		
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(boss, worker)
					.channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.INFO))
					.option(ChannelOption.SO_BACKLOG, 1024)
					.childOption(ChannelOption.TCP_NODELAY, true)
					.childOption(ChannelOption.SO_KEEPALIVE, true)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel socketChannel) throws Exception {
							ChannelPipeline pipeline = socketChannel.pipeline();
							
							//	编码
							
							
							//	解码
							
						}
					});
			
			//	服务端绑定端口并启动
			ChannelFuture future = bootstrap.bind(serverConfiguration.getRpcPort()).sync();
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			boss.shutdownGracefully();
			worker.shutdownGracefully();
			business.shutdownGracefully();
		}
	}
}
