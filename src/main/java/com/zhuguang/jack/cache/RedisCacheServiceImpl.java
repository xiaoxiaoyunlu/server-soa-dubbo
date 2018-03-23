package com.zhuguang.jack.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisCacheServiceImpl implements BaseCacheService {
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Override
	public void setKeyValue(String key, Object obj) throws Exception {
          set(key,obj.toString());
	}
	
	@Override
	public void setKeyValue(String key, Object obj, Integer expire, TimeUnit unit)
			throws Exception {
		setTime(key, obj, expire, unit);
		
	}	

	@Override
	public Object getKeyValue(String key) throws Exception {
		return get(key);
	}

	@Override
	public void removeKey(String key) throws Exception {
           delete(key);
	}
	
	@Override
	public boolean exists(String key) throws Exception {
		return redisTemplate.hasKey(key);
	}
	
	/* ----------- common --------- */
	public Collection<String> keys(String pattern) {
		return redisTemplate.keys(pattern);
	}

	public void delete(String key) {
		redisTemplate.delete(key);
	}

	public void delete(Collection<String> key) {
		redisTemplate.delete(key);
	}
	
	/**
	 * 普通set 自增
	 * 
	 * @param key
	 */
	public void autoIncre(String key) {
		redisTemplate.boundValueOps(key).increment(1);
	}

	public void autoDecre(String key) {
		redisTemplate.boundValueOps(key).increment(-1);
	}

	/**
	 * hset 自增
	 * 
	 * @param key
	 * @param field
	 */
	public void autoHIncre(String key, String field) {
		redisTemplate.boundHashOps(key).increment(field, 1);
	}

	public void autoHDecre(String key, String field) {
		redisTemplate.boundHashOps(key).increment(field, -1);
	}

	/* ----------- string --------- */
	public String get(String key) {
		Object value = redisTemplate.boundValueOps(key).get();
		return (String) value;
	}

	public void set(String key, String obj) {
		if (obj == null) {
			return;
		}
		redisTemplate.boundValueOps(key).set(obj);

	}

	public void hset(String key, String field, String obj) {
		redisTemplate.boundHashOps(key).put(field,obj);
	}

	public String hget(String key, String field) {
		return (String) redisTemplate.boundHashOps(key).get(field);
	}

	public List<Object> hget(String key) {
		List<Object> list = new ArrayList<>();
		Set<Object> fileds = redisTemplate.boundHashOps(key).keys();
		for (Object o : fileds) {
			list.add(redisTemplate.boundHashOps(key).get(o));
		}
		return list;
	}
	/**
	 * 获取对应的hash的  所有 filed 对应的所有key  等同于上面的hget
	 * @param key
	 * @return
	 */
	public List<Object> hGetAllValue(String key){
		return redisTemplate.boundHashOps(key).values();
	}
	/**
	 * 获取hash 的所有  filed
	 * @param key
	 * @return
	 */
	public Set<Object> hGetAllKeys(String key){
		return redisTemplate.boundHashOps(key).keys();
	}
	/**
	 * 获取hash 所有 filed value  键值对
	 * @param key
	 * @return
	 */
	public Map<Object, Object> hGetAllFiledValue(String key){
		return redisTemplate.boundHashOps(key).entries();
	}
	
	
	public void hSetMap(String key,Map<Object,Object> map){
		redisTemplate.boundHashOps(key).putAll(map);
	}
	

	public Set<Object> hgetKeys(String key) {
		return redisTemplate.boundHashOps(key).keys();
	}

	public void setTime(String key, Object obj, Integer timeout, TimeUnit unit) {
		if (obj == null) {
			return;
		}

		if (timeout != null) {
			redisTemplate.boundValueOps(key).set(obj, timeout, unit);
		} else {
			redisTemplate.boundValueOps(key).set(obj);
		}
	}
	
	
	/*-------------------list-------------------------start---------*/
	/**
	 * list 入队     
	 */
	public void listLPush(String key,Object obj){
		redisTemplate.boundListOps(key).leftPush(obj);
	}
	/**
	 * list  批量入队
	 * @param key
	 * @param list
	 */
	public void listPushAll(String key,List list){
		redisTemplate.boundListOps(key).leftPushAll(list);
	}
	
	/**
	 * list  出队   队列先进先出    左边进去，右边出
	 * @return
	 */
	public Object listRPop(String key){
         return redisTemplate.boundListOps(key).rightPop();               		
	}
	
	/**
	 * list  出栈   后进先出   左边进去，左边出
	 * @return
	 */
	public Object listLPop(String key){
		return redisTemplate.boundListOps(key).leftPop();
	}

	

	

}
