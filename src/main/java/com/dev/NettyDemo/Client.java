package com.dev.NettyDemo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {
	
	public static void main(String[] args) throws Exception {
		Client client=new Client();
		String msg="Hello";
		client.sendMsg(msg);
	}

	private  void sendMsg(String msg) throws InterruptedException {
		EventLoopGroup pGroup = new NioEventLoopGroup();

		Bootstrap b = new Bootstrap();
		b.group(pGroup)
		.channel(NioSocketChannel.class)
		.handler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel sc) throws Exception {
				sc.pipeline().addLast(new ClientHandler());

			}

		});

		ChannelFuture cf1 = b.connect(Constants.ip, Constants.port).sync();
		
		cf1.channel().writeAndFlush(Unpooled.copiedBuffer(msg.getBytes()));
		
		cf1.channel().closeFuture().sync();
		pGroup.shutdownGracefully();
	}
}
