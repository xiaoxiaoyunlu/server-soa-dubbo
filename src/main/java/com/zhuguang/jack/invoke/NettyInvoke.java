package com.zhuguang.jack.invoke;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.zhuguang.jack.configBean.Reference;
import com.zhuguang.jack.loadbalance.LoadBalance;
import com.zhuguang.jack.loadbalance.NodeInfo;
import com.zhuguang.jack.utils.HttpRequestUtil;
import com.zhuguang.jack.utils.NettyUtils;

public class NettyInvoke implements Invoke {

	@Override
	public String invoke(Invocation invocation) throws Exception {
		Reference reference;
		try {
			//先获取服务可用列表
			reference=invocation.getReference();
            List<String> registryInfo=reference.getRegistryInfo();
            //获取负载均衡算法
            String loadbalance=reference.getLoadbalance();
            LoadBalance loadBalance=reference.getLoadBalances().get(loadbalance);
            
            NodeInfo nodeInfo=loadBalance.doSelect(registryInfo);
            
            //我们远程调用的生产者传输的是json
            //根据serviceid 去生产者的spring容器中获取serviceid对应的实例
            //根据methodName 和 MethodType获取method的对象
            //反射调用method方法
            
            JSONObject sendparam=new JSONObject();
            sendparam.put("methodName", invocation.getMethod().getName());
            sendparam.put("methodParams", invocation.getMethod().getParameters());
            sendparam.put("serviceId", reference.getId());
            sendparam.put("paramTypes", invocation.getMethod().getParameterTypes());
            
            //http://127.0.0.1:8023/jack/soa/service
            //调用对端的生产者的服务
            String result=NettyUtils.sendMsg(nodeInfo.getHost(),nodeInfo.getPort(),sendparam.toJSONString());
            
            return result;
			
			
		} catch (Exception e) {
		     //异常指直接抛出去？
			 throw e;
		}
	}

}
