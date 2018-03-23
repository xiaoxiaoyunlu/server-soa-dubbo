package com.zhuguang.jack.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyClientInHandler extends ChannelInboundHandlerAdapter {

	private String response;
	
	public NettyClientInHandler(String sendmsg) {
		//发送请求给服务器，调用服务端service服务
		byte[] reqMsg=sendmsg.getBytes();
		ByteBuf byteBuf = Unpooled.buffer(reqMsg.length);  
        byteBuf.writeBytes(reqMsg);
        byteBuf.release();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		ByteBuf buf=(ByteBuf)msg;
		 byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "UTF-8");
        response=body;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
	}

	public String getResponse() {
		return response;
	}


}
