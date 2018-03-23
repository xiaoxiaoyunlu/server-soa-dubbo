package com.zhuguang.jack.cache;

import java.util.concurrent.TimeUnit;

public interface BaseCacheService {
	
	//放缓存
	public void setKeyValue(String key,Object vlaue)throws Exception;
	
	//放缓存 过期时间
    public void setKeyValue(String key,Object vlaue,Integer expire,TimeUnit unit)throws Exception;
	
	
	//获取缓存？
	public Object getKeyValue(String key)throws Exception;
	
	
	//清理缓存
	public void removeKey(String key)throws Exception;
	
	//是否存在key
	public boolean exists(String key)throws Exception;
	

}
