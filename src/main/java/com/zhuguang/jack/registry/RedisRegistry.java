package com.zhuguang.jack.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhuguang.jack.configBean.Protocol;
import com.zhuguang.jack.configBean.Registry;
import com.zhuguang.jack.configBean.Service;
import com.zhuguang.jack.redis.RedisApi;

/**
 * 使用redis 实现的注册中心类
 * @author USER
 *
 */
public class RedisRegistry implements BaseRegistry {

	@Override
	public boolean registry(String ref, ApplicationContext application) {
		try {
			Protocol protocol=application.getBean(Protocol.class);
			//获取项目application-provider.xml配置的service 列表
			Map<String, Service> services=application.getBeansOfType(Service.class);
			
			Registry registry=application.getBean(Registry.class);
			RedisApi.createJedisPool(registry.getAddress());
			
            for(Map.Entry<String, Service> entry:services.entrySet()){
            	if(entry.getValue().getRef().equals(ref)){
            		JSONObject jo=new JSONObject();
            		jo.put("protocol", JSON.toJSONString(protocol));
            		jo.put("service", JSON.toJSONString(entry.getValue()));
            		
            		JSONObject ipport=new JSONObject();
            		ipport.put(protocol.getHost()+":"+protocol.getPort(), jo);
            		lpush(ipport,ref);
            	}
            }
            return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private void lpush(JSONObject ipport, String key) {
		if(RedisApi.exists(key)){
			Set<String> keys=ipport.keySet();
			String ipportStr="";
			for(String kk:keys){
				ipportStr=kk;
			}
			
			// Redis 对应key里面的内容
			List<String> registryInfo=RedisApi.lrange(key);
			List<String> newRegistry=new ArrayList<String>();
			
			boolean isold=false;
			for(String node:registryInfo){
				JSONObject jo=JSONObject.parseObject(node);
				if(jo.containsKey(ipportStr)){
					newRegistry.add(ipport.toJSONString());
					isold=true;
				}
			}
			
			if(isold){
				//这里是老机器启动去重？   
				if(newRegistry.size()>0){
					RedisApi.del(key);
					String[] newReStr=new String[newRegistry.size()];
					for (int i = 0; i < newRegistry.size(); i++) {
						newReStr[i]=newRegistry.get(i);
					}
					RedisApi.lpush(key,ipport.toJSONString());
				}
			}else{
				//这里是加入新启动的机器
				RedisApi.lpush(key,ipport.toJSONString());
			}
		}else{
			//所有的都是第一次启动？
			RedisApi.lpush(key,ipport.toJSONString());
		}
		
		
	}

	/**
	 *id 是  接口service的全路径名称  
	 *  com.zhuguang.jack.service.UserService
	 */
	@Override
	public List<String> getRegistry(String id, ApplicationContext application) {
		try {
			Registry registry=application.getBean(Registry.class);
			RedisApi.createJedisPool(registry.getAddress());
			if(RedisApi.exists(id)){
				// key 对应的list
				return RedisApi.lrange(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
