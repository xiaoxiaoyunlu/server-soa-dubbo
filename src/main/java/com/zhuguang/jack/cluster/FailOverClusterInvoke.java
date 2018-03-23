package com.zhuguang.jack.cluster;

import com.zhuguang.jack.invoke.Invocation;
import com.zhuguang.jack.invoke.Invoke;
import com.zhuguang.jack.utils.StringUtils;

/**
 * 调用失败，就自动切换到集群其他可用节点？
 * 如何自动？ 从 可用的其他列表中选择嘛？
 * @author USER
 *
 */
public class FailOverClusterInvoke implements Cluster {

	@Override
	public String invoke(Invocation invocation) throws Exception {
		String retries=invocation.getReference().getRetries();
		int retry=Integer.parseInt(retries);
		//尽量避免在循环中 try catch
		//遗留问题，这总情况下，如何合理使用异常
		String result;
			for(int i=0;i<retry;i++){
				try {
					Invoke invoke=invocation.getInvoke();
					result= invoke.invoke(invocation);
					if(!StringUtils.isEmpty(result)){
						return result;
					}
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				
				
			}
		throw new RuntimeException("retries:"+retry+"次全部失败");
	}

}
