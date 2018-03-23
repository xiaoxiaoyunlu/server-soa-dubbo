package com.zhuguang.jack.registry;

import java.util.List;

import org.springframework.context.ApplicationContext;

/**
 * 注册中心接口
 * @author USER
 *
 */
public interface BaseRegistry {

	/**
	 * 注册
	 * @param ref
	 * @param application
	 */
	public boolean registry(String ref, ApplicationContext application);

	/**
	 * 获取注册的服务列表
	 * @param id
	 * @param application
	 * @return
	 */
	public List<String> getRegistry(String id, ApplicationContext application);
	
	

}
