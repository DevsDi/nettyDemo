package com.dev.NettyDemo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {
	public static void main(String[] args) throws Exception {
		EventLoopGroup pGroup = new NioEventLoopGroup();//处理服务器与客户端连接
		EventLoopGroup cGroup = new NioEventLoopGroup();//网络通信用

		ServerBootstrap b = new ServerBootstrap();//辅助工具类，用于服务器通道配置
				b.group(pGroup, cGroup)//绑定线程组
				.channel(NioServerSocketChannel.class)//知道NIO模式
				.option(ChannelOption.SO_BACKLOG, Constants.backlog)//设置TCP缓冲区大小
				.option(ChannelOption.SO_SNDBUF, Constants.sndbuf)//设置发送缓冲区大小
				.option(ChannelOption.SO_RCVBUF, Constants.rcvbuf)//设置接受缓冲区大小
				.option(ChannelOption.SO_KEEPALIVE, Constants.keepalive)//保持连接
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel sc) throws Exception {
						sc.pipeline().addLast(new ServerHandler());//配置具体数据接收方法的处理
					}

				});

		ChannelFuture cf1 = b.bind(Constants.port).sync();//绑定
		System.out.println("Server Start...");
		
		
		cf1.channel().closeFuture().sync();//阻塞，等待关闭
		
		pGroup.shutdownGracefully();
		cGroup.shutdownGracefully();
	}
}
