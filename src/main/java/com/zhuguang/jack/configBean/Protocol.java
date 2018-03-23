package com.zhuguang.jack.configBean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.zhuguang.jack.utils.NettyUtils;
import com.zhuguang.jack.utils.RmiUtils;

public class Protocol extends BaseConfigBean implements InitializingBean,
		ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {

	private static final long serialVersionUID = -6991000802041614098L;
	private String name;
	private String port;
	private String host;
	private String contextPath; 

	private static ApplicationContext application;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
 
	public static ApplicationContext getApplication() {
		return application;
	}

	public static void setApplication(ApplicationContext applicationContext) {
		Protocol.application = applicationContext;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 这个事件是spring启动完成后触发的事件
	 */
	@Override
	public void setApplicationContext(ApplicationContext event)
			throws BeansException {
		if(!ContextRefreshedEvent.class.getName().equals(event.getClass().getName())){
			return;
		}
		
		if(!"netty".equals(name)){
			return;
		}
		
        new Thread(new Runnable() {
			
			@Override
			public void run() {
                 try {
					NettyUtils.startServer(port);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}				
			}
		}).start();		
		
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		  if(name.equalsIgnoreCase("rmi")){
			  RmiUtils rmi=new RmiUtils();
			  rmi.startRmiServer(host, port, "jacksoarmi");
		  }
		
	}

}
