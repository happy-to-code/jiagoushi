package com.yida;

import com.yida.codec.ProtoStuffDecoder;
import com.yida.handler.server.ServerInBoundHandler1;
import com.yida.handler.server.TcpStickHalfHandler1;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.NettyRuntime;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.UnorderedThreadPoolEventExecutor;

public class NettyServer {
	public static void main(String[] args) {
		NettyServer server = new NettyServer();
		server.start(9999);
	}
	
	private void start(int port) {
		// BossGroup中的线程专门负责和客户端建立连接 启动一个线程就够了  new DefaultThreadFactory("boss") 是给group起一个名字，方便调试
		EventLoopGroup boss = new NioEventLoopGroup(1, new DefaultThreadFactory("boss"));
		// work group 线程数参数传0  底层会自动给其分配是cpu核数*2
		// private static final int DEFAULT_EVENT_LOOP_THREADS = Math.max(1, SystemPropertyUtil.getInt("io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2));
		EventLoopGroup worker = new NioEventLoopGroup(0, new DefaultThreadFactory("worker"));
		EventExecutorGroup business = new UnorderedThreadPoolEventExecutor(NettyRuntime.availableProcessors() * 2, new DefaultThreadFactory("business"));
		
		try {
			// netty启动引导类
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(boss, worker)
					.channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 1024)
					.childOption(ChannelOption.SO_KEEPALIVE, true)
					.childOption(ChannelOption.TCP_NODELAY, true)
					.handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new ChannelInitializer<SocketChannel>() {
						// 当客户端 SocketChannel初始化时回调该方法,添加handler
						@Override
						protected void initChannel(SocketChannel socketChannel) {
							ChannelPipeline pipeline = socketChannel.pipeline();
							
							
							// 以每一行进行切割
							// pipeline.addLast(new LineBasedFrameDecoder(65536));
							
							// 以特定字符进行分割
							// ByteBuf buf = socketChannel.alloc().buffer().writeBytes("$$".getBytes(StandardCharsets.UTF_8));
							// pipeline.addLast(new DelimiterBasedFrameDecoder(65536, buf));
							
							// 以固定长度指定数据大小
							pipeline.addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 4));
							pipeline.addLast("protostuffdecoder",new ProtoStuffDecoder());
							// pipeline.addLast(new ProtobufDecoder(MessageProto.Message.getDefaultInstance()));
							// 读取客户端发送过来的数据
							// pipeline.addLast("ServerInBoundHandler1", new ServerInBoundHandler1());
							pipeline.addLast("TcpStickHalfHandler1", new TcpStickHalfHandler1());
						}
					});
			
			// 绑定端口并启动 走同步方法
			ChannelFuture future = serverBootstrap.bind(port).sync();
			
			// 监听端口的关闭
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			// 释放资源
			worker.shutdownGracefully();
			boss.shutdownGracefully();
			business.shutdownGracefully();
		}
	}
}
