package com.zhuguang.jack.registry;

import java.util.List;

import org.springframework.context.ApplicationContext;

import com.zhuguang.jack.configBean.Registry;

/**
 * 委托代理，封装注册和获取注册服务列表
 * 防止变更 注册中心的实现方式，可以变更少量代码
 * 比如现在用redis  以后   更换成  memcache  Echached   mongo   ZK  等等
 * @author USER
 *
 */
public class BaseRegistryDelegate {

	public static void registry(String ref, ApplicationContext application) {
		
		Registry registry=application.getBean(Registry.class);
		String protocol=registry.getProtocol();
		BaseRegistry registryBean=registry.getRegistryMap().get(protocol);
		registryBean.registry(ref,application);
	}
	
	public static List<String> getRegisty(String id,ApplicationContext application){
		Registry registry=application.getBean(Registry.class);
		String protocol=registry.getProtocol();
		BaseRegistry registryBean=registry.getRegistryMap().get(protocol);
		
		return registryBean.getRegistry(id,application);
	}

}
