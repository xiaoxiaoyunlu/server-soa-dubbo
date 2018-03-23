package com.zhuguang.jack.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;

public class MethodUtils {

	/**
	 * 考虑到重载的情况，找到正确的方法
	 * @param serviceBean
	 * @param methodName
	 * @param paramTypes
	 * @return
	 */
	public static  Method getMethod(Object serviceBean, String methodName,
			JSONArray paramTypes) {
		Method[] methods=serviceBean.getClass().getMethods();
		List<Method> resultMethod=new ArrayList<Method>();
		for(Method m:methods){
			//判断方法名字和参数个数
			if(m.getName().equals(methodName)&& m.getParameterTypes().length==paramTypes.size()){
				resultMethod.add(m);
			}
		}
		
		//只有一个 说明，没有重载方法
		if(resultMethod.size()==1){
			return resultMethod.get(0);
		}else{
			//遗留问题 ，如果有重载的方法，怎么搞？
			//判断参数类型
		  for(Method m:resultMethod){
			  if(m.getParameterTypes().toString().equals(paramTypes.toJSONString())){
				  return m;
			  }
		  }
		}
		
		return null;
	}
	
}
