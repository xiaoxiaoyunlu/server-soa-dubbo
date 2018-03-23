package com.zhuguang.jack.invoke;

/**
 * 返回response  用json方式通信
 * @author USER
 *
 */
public interface Invoke {
	
	public String invoke(Invocation invocation)throws Exception;

}
