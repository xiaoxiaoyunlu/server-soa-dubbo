package com.zhuguang.jack.proxy.advice;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.zhuguang.jack.cluster.Cluster;
import com.zhuguang.jack.configBean.Reference;
import com.zhuguang.jack.invoke.Invocation;
import com.zhuguang.jack.invoke.Invoke;

/**
 * InvocationHandler 实现了这个接口的处理类advice中实现了rpc远程调用
 * @author USER
 *
 */
public class InvokeInvocationHandler implements InvocationHandler {
	
	private Invoke invoke;
	private Reference reference;

	public InvokeInvocationHandler(Invoke invoke, Reference reference) {
		this.invoke = invoke;
		this.reference = reference;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		
		//  invoke里面最终要调用多个远程provider的服务
		System.out.println("已经到了远程调用代理实例，进入InvokeInvocationHandler.invoke方法中");
		Invocation invocation=new Invocation();
		invocation.setMethod(method);
		invocation.setObjs(args);
		invocation.setReference(reference);
		invocation.setInvoke(invoke);
		
		Cluster cluster=reference.getClusters().get(reference.getCluster());
		String result=cluster.invoke(invocation);
		
		return result;
	}

}
