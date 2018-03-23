package com.zhuguang.jack.rmi;

import java.awt.geom.IllegalPathStateException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhuguang.jack.configBean.Service;
import com.zhuguang.jack.utils.MethodUtils;

/**
 * RMI的实现类  负责rmi调用  这是生产者端使用的类  启动生产者端？
 * 
 * @author USER
 *
 */
public class SoaRmiImpl extends UnicastRemoteObject implements SoaRmi {

	private static final long serialVersionUID = 6506345365545077958L;

	public SoaRmiImpl() throws RemoteException {
		super();
	}

	@Override
	public String invoke(String param) throws RemoteException {
		JSONObject requestparam=JSONObject.parseObject(param);
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
			try {
				result=method.invoke(serviceBean, objs);
				return result.toString();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}catch(IllegalArgumentException e){
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			
		}
		return null;
		
	}

}
