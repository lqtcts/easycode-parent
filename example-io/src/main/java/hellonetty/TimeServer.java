package hellonetty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TimeServer {

	public void bind(int port) throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			/* 配置服务端的NIO线程组 */
			// NioEventLoopGroup类 是个线程组，包含一组NIO线程，用于网络事件的处理
			// （实际上它就是Reactor线程组）。
			// 创建的2个线程组，1个是服务端接收客户端的连接，另一个是进行SocketChannel的
			// 网络读写
			// 配置服务器的NIO线程租
			ServerBootstrap b = new ServerBootstrap();
			ServerBootstrap channel = b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class);
			channel.option(ChannelOption.SO_BACKLOG, 1024);
			channel.childHandler(new ChildChannelHandler());
			// 绑定端口，同步等待成功
			ChannelFuture f = b.bind(port).sync();
			// 等待服务端监听端口关闭
			f.channel().closeFuture().sync();
		} finally {
			// 释放线程池资源
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(SocketChannel arg0) throws Exception {
			System.out.println("server initChannel..");
			arg0.pipeline().addLast(new TimeServerHandler());
		}
	}

	public static void main(String[] args) throws Exception {
		int port = 9000;
		if (args != null && args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (NumberFormatException e) {

			}
		}

		new TimeServer().bind(port);
	}
}