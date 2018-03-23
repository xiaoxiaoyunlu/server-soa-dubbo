package com.zhuguang.jack.netty;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhuguang.jack.configBean.Service;
import com.zhuguang.jack.utils.MethodUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyServerInHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelRegistered(ctx);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelUnregistered(ctx);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelActive(ctx);
	}

	/**
	 * netty的客户端有消息过来就会调用这个方法
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
           ByteBuf req=(ByteBuf)msg;
           byte[] reqByte=new byte[req.readableBytes()];
           req.readBytes(reqByte);
           String reqStr=new String(reqByte);
           
           req.release();
           
           String response=invokeService(reqStr);
           ByteBuf encoded=ctx.alloc().buffer(4*response.length());
           encoded.writeBytes(response.getBytes());
           ctx.writeAndFlush(encoded);
           ctx.close();
	}

	private String invokeService(String reqStr) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		JSONObject requestparam=JSONObject.parseObject(reqStr);
		//要从远程的生产者的spring容器中获取对应的serviceid的实例
		String serviceId=requestparam.getString("serviceId");
		String methodName=requestparam.getString("methodName");
		JSONArray paramTypes=requestparam.getJSONArray("paramTypes");
		JSONArray methodParams=requestparam.getJSONArray("methodParams");
		
		//这个就是反射的参数？
		Object[] objs=null;
		if(null!=methodParams){
			objs=new Object[methodParams.size()];
			int i=0;
			for(Object o:methodParams){
				objs[i++]=o;
			}
		}
		
		//拿到spring的上下文对象
		ApplicationContext application=Service.getApplication();
		//服务的实例对象
		Object serviceBean=application.getBean(serviceId);
		//这个方法的获取，要考虑到方法的重载
		Method method=MethodUtils.getMethod(serviceBean,methodName,paramTypes);
		if(null!=method){
			Object result;
			result=method.invoke(serviceBean, objs);
			if(null!=result){
				return result.toString();
			}
		}
		return null;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
	}

}
