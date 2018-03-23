package com.zhuguang.jack.cache;

import java.util.List;

/**
 * 缓存管理器   弃用 
 * 项目 多个缓存启动的可能性不大
 * spring cache  也只是封装了抽象管理CacheManager
 * 具体的缓存动作还是要具体的redis  memcache等缓存框架去实现
 * @author USER
 *
 */
public interface BaseCacheManager {

	//加载配置的缓存列表
	public List<BaseCacheService> loadCaches()throws Exception;
	
	
	
}
