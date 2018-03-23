package com.zhuguang.jack.loadbalance;

import java.util.List;

public interface LoadBalance {

	public NodeInfo doSelect(List<String> registryInfo)throws Exception;
	
	

}
