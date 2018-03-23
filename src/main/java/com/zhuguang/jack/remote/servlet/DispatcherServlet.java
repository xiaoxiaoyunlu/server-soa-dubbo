package com.zhuguang.jack.remote.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhuguang.jack.configBean.Service;
import com.zhuguang.jack.utils.GetRequestJsonUtils;
import com.zhuguang.jack.utils.MethodUtils;


/**
 * 这个servlet 需要配置到 web.xml里面
 * 刚开始，不知道为什么要配置这类
 * 其实现在是做的dubbo的框架处理，服务端本身也配置有dubbo
 * 客户端的请求来调用服务端实例，要先进入到duboo框架本身处理
 * 就好比springmvc中的DispatcherSevlet 一样
 * 现在服务端  启动后，是dubbo框架在处理，而不是服务端 的controller 来接收
 * @author USER
 *
 */
public class DispatcherServlet extends HttpServlet {

	private static final long serialVersionUID = 6706342361586642799L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String postjson=GetRequestJsonUtils.getRequestJsonString(request);
			JSONObject requestparam=JSONObject.parseObject(postjson);
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
			
			//返回给consumer调用者
			PrintWriter out;
			if(null!=method){
				Object result;
				result=method.invoke(serviceBean, objs);
				out=response.getWriter();
				out.write(result.toString());
			}else{
				out=response.getWriter();
				out.write("--------no such method find---------");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	

}
