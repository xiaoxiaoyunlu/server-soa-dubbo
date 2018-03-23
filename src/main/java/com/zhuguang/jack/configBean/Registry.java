package com.zhuguang.jack.configBean;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.zhuguang.jack.registry.BaseRegistry;
import com.zhuguang.jack.registry.RedisRegistry;

public class Registry extends BaseConfigBean implements InitializingBean,ApplicationContextAware {
	
	public ApplicationContext application;
	
	private static Map<String,BaseRegistry> registryMap=new HashMap<String, BaseRegistry>();

	static{
		//只能配置一个 ，或者以后看看是否可以优化？
		registryMap.put("redis",new RedisRegistry());
//		registryMap.put("memcache",new MemcachedRegistry());
//		registryMap.put("echcache",new EchCacheRegistry());
//		registryMap.put("mongo",new MongoRegistry());
	}
	
	//注册中心采用的协议
	private String protocol;
	//注册中的URL地址
	private String address;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.application=applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public ApplicationContext getApplication() {
		return application;
	}

	public void setApplication(ApplicationContext application) {
		this.application = application;
	}

	public static Map<String, BaseRegistry> getRegistryMap() {
		return registryMap;
	}

}
