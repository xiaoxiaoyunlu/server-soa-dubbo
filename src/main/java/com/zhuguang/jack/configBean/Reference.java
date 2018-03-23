package com.zhuguang.jack.configBean;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.zhuguang.jack.cluster.Cluster;
import com.zhuguang.jack.cluster.FailFastClusterInvoke;
import com.zhuguang.jack.cluster.FailOverClusterInvoke;
import com.zhuguang.jack.cluster.FailSafeClusterInvoke;
import com.zhuguang.jack.invoke.HttpInvoke;
import com.zhuguang.jack.invoke.Invoke;
import com.zhuguang.jack.invoke.NettyInvoke;
import com.zhuguang.jack.invoke.RmiInvoke;
import com.zhuguang.jack.loadbalance.LoadBalance;
import com.zhuguang.jack.loadbalance.RandomLoadBalance;
import com.zhuguang.jack.loadbalance.RoundRobBalance;
import com.zhuguang.jack.proxy.advice.InvokeInvocationHandler;
import com.zhuguang.jack.registry.BaseRegistryDelegate;
import com.zhuguang.jack.utils.StringUtils;

public class Reference extends BaseConfigBean implements FactoryBean,
		InitializingBean, ApplicationContextAware {

	private static final long serialVersionUID = 6619835545201802887L;

	private String id;

	private String intf;
	// 负载均衡策略
	private String loadbalance;
	// 使用的协议
	private String protocol;
	// 集群设置？
	private String cluster;
	// 重试次数
	private String retries;
	private ApplicationContext application;
	private Invoke invoke;

	private static Map<String, Invoke> invokes = new HashMap<String, Invoke>();
	private static Map<String, LoadBalance> loadBalances = new HashMap<String, LoadBalance>();
	private static Map<String, Cluster> clusters = new HashMap<String, Cluster>();

	// 生产者的多个服务的列表
	private List<String> registryInfo = new ArrayList<String>();

	static {
		//配置调用的rpc协议
		invokes.put("http", new HttpInvoke()); 
		invokes.put("rmi", new RmiInvoke());
		invokes.put("netty", new NettyInvoke());
		//配置可选的负载均衡策略
		loadBalances.put("random",new RandomLoadBalance());
		loadBalances.put("roundrob",new RoundRobBalance());
        //配置可选的集群容错机制处理
		clusters.put("failover",new FailOverClusterInvoke());
		clusters.put("failfast",new FailFastClusterInvoke());
		clusters.put("failsafe",new FailSafeClusterInvoke());
		
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// 获取注册中心的服务列表， 存放在本地，
		// 后面可能会有定时更新策略
		registryInfo = BaseRegistryDelegate.getRegisty(id, application);
		System.out.println(registryInfo);
	}

	/**
	 * Proxy.newProxyInstance
	 * 返回对象实例交给spring管理，可以根据spring的上下文，拿到实例去调用
	 */
	@Override
	public Object getObject() throws Exception {
		System.out.println("Reference getObject");
		if (!StringUtils.isEmpty(protocol)) {
			invoke = invokes.get(protocol);
		} else {
			Protocol pro = application.getBean(Protocol.class);
			if (null != pro) {
				invoke = invokes.get(pro);
			} else {
				// 默认使用http
				invoke = invokes.get("http");
			}
		}
		return Proxy.newProxyInstance(this.getClass().getClassLoader(),
				new Class<?>[] { Class.forName(intf) },
				new InvokeInvocationHandler(invoke, this));
	}

	@Override
	public Class getObjectType() {
		try {
			if(!StringUtils.isEmpty(intf)){
				return Class.forName(intf);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIntf() {
		return intf;
	}

	public void setIntf(String intf) {
		this.intf = intf;
	}

	public String getLoadbalance() {
		return loadbalance;
	}

	public void setLoadbalance(String loadbalance) {
		this.loadbalance = loadbalance;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getCluster() {
		return cluster;
	}

	public void setCluster(String cluster) {
		this.cluster = cluster;
	}

	public String getRetries() {
		return retries;
	}

	public void setRetries(String retries) {
		this.retries = retries;
	}

	public ApplicationContext getApplication() {
		return application;
	}

	public void setApplication(ApplicationContext application) {
		this.application = application;
	}

	public Invoke getInvoke() {
		return invoke;
	}

	public void setInvoke(Invoke invoke) {
		this.invoke = invoke;
	}

	public static Map<String, Invoke> getInvokes() {
		return invokes;
	}

	public static void setInvokes(Map<String, Invoke> invokes) {
		Reference.invokes = invokes;
	}

	public static Map<String, LoadBalance> getLoadBalances() {
		return loadBalances;
	}

	public static void setLoadBalances(Map<String, LoadBalance> loadBalances) {
		Reference.loadBalances = loadBalances;
	}

	public static Map<String, Cluster> getClusters() {
		return clusters;
	}

	public static void setClusters(Map<String, Cluster> clusters) {
		Reference.clusters = clusters;
	}

	public List<String> getRegistryInfo() {
		return registryInfo;
	}

	public void setRegistryInfo(List<String> registryInfo) {
		this.registryInfo = registryInfo;
	}

}
