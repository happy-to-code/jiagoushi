package netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldPrepender;
import netty.handler.client.ClientInboundHandler1;

import java.nio.charset.StandardCharsets;

public class NettyClient {
	public static void main(String[] args) {
		NettyClient client = new NettyClient();
		client.start("127.0.0.1", 9999);
	}
	
	private void start(String host, int port) {
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		
		bootstrap.group(group)
				.channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel socketChannel) throws Exception {
						ChannelPipeline pipeline = socketChannel.pipeline();
						
						// 以固定长度指定数据大小
						pipeline.addLast(new LengthFieldPrepender(4));
						pipeline.addLast("ClientInboundHandler1", new ClientInboundHandler1());
					}
				});
		try {
			ChannelFuture future = bootstrap.connect(host, port).sync();
			
			//  客户端向服务端写数据 (此操作不能放在关闭资源代码下面)
			// writeData2Server(future);
			
			// 监听关闭资源
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
		
	}
	
	private void writeData2Server(ChannelFuture future) {
		//	1、获取通道
		Channel channel = future.channel();
		//  2、获取缓存空间
		ByteBuf buf = channel.alloc().buffer();
		//	3、向缓存空间中存入数据
		buf.writeBytes("hello NettyServer,i am yida".getBytes(StandardCharsets.UTF_8));
		//	向通道中写入数据
		channel.writeAndFlush(buf);
	}
}
