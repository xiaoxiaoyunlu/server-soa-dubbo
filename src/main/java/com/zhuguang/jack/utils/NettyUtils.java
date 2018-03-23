package com.zhuguang.jack.utils;

import com.zhuguang.jack.netty.NettyClientInHandler;
import com.zhuguang.jack.netty.NettyServerInHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class NettyUtils {

	public static String sendMsg(String host, String port, String sendmsg) {
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		NettyClientInHandler cosumerHandler=new NettyClientInHandler(sendmsg);
		try {
			Bootstrap b = new Bootstrap();
			b.group(workerGroup);
			b.channel(NioSocketChannel.class);
			b.handler(new ChannelInitializer<Channel>() {

				@Override
				protected void initChannel(Channel ch) throws Exception {
					 // 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
                    ch.pipeline().addLast(new HttpResponseDecoder());
                    // 客户端发送的是httprequest，所以要使用HttpRequestEncoder进行编码
                    ch.pipeline().addLast(new HttpRequestEncoder());
					ch.pipeline().addLast(cosumerHandler);
				}
			});
			// 这是连接服务端，长连接，一直等着服务端回复消息
			// 返回的信息封装到Future,可以监控到线程等我返回
			ChannelFuture future = b.connect(host, Integer.parseInt(port));
			future.channel().closeFuture().await();
			return cosumerHandler.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			workerGroup.shutdownGracefully();
		}
		return sendmsg;
	}

	public static void startServer(String port) throws NumberFormatException, InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<Channel>() {

						@Override
						protected void initChannel(Channel ch) throws Exception {
							 // server端发送的是httpResponse，所以要使用HttpResponseEncoder进行编码
                            ch.pipeline().addLast(new HttpResponseEncoder());
                            // server端接收到的是httpRequest，所以要使用HttpRequestDecoder进行解码
                            ch.pipeline().addLast(new HttpRequestDecoder());
                            ch.pipeline().addLast(new NettyServerInHandler());							
						}
					}).option(ChannelOption.SO_BACKLOG, 128);
			ChannelFuture future=b.bind(Integer.parseInt(port)).sync();
			future.channel().closeFuture().sync();
		}finally{
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}

	}
}
