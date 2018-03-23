package com.zhuguang.jack.configBean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.zhuguang.jack.registry.BaseRegistryDelegate;

public class Service  extends BaseConfigBean implements InitializingBean,ApplicationContextAware{
	
	
	private static final long serialVersionUID = -2922784138452909455L;
	
	private String intf;
	private String ref;
	private String protocol;
	
	private static ApplicationContext application;
	

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
          this.application=applicationContext;		
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		//spring 加载初始化之后，注册到 注册中心
		//利用委托代理    去 封住注册， 防止以后变更注册方式，可以代码改动很少
		BaseRegistryDelegate.registry(ref,application);
	}

	public String getIntf() {
		return intf;
	}

	public void setIntf(String intf) {
		this.intf = intf;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public static ApplicationContext getApplication() {
		return application;
	}

	public static void setApplication(ApplicationContext application) {
		Service.application = application;
	}

}
