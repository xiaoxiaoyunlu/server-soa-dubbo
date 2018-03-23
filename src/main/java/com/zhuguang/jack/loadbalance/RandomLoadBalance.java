package com.zhuguang.jack.loadbalance;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import com.alibaba.fastjson.JSONObject;

/**
 * 随机的负载均衡算法
 * @author USER
 *
 */
public class RandomLoadBalance implements LoadBalance {

	@Override
	public NodeInfo doSelect(List<String> registryInfo) throws Exception {
		Random random=new Random();
		int index=random.nextInt(registryInfo.size());
		//获取的是 registry的json字符串
		String registry=registryInfo.get(index);
		
		JSONObject registryJo=JSONObject.parseObject(registry);
		Collection values=registryJo.values();
		JSONObject node=new JSONObject();
		//不理解，待确认
		for(Object value:values){
			node=JSONObject.parseObject(value.toString());
		}
		JSONObject protocol=node.parseObject("potocol");
		NodeInfo nodeInfo=new NodeInfo();
		nodeInfo.setHost(protocol.getString("host")!=null?protocol.getString("host"):"");
		nodeInfo.setPort(protocol.getString("port")!=null?protocol.getString("port"):"");
		nodeInfo.setContextpath(protocol.getString("contextpath")!=null?protocol.getString("contextpath"):"");
		return nodeInfo;
	}

}
