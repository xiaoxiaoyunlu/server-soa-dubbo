package com.zhuguang.jack.spring.parse;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import com.zhuguang.jack.configBean.Reference;

public class ReferenceBeanDefinitionParser implements BeanDefinitionParser {
	
	private final Class<?> beanClass;
    
    protected Class getBeanClass(Element element) {  
        return Reference.class;  
    }
    
    public ReferenceBeanDefinitionParser(Class<?> beanClass){
    	this.beanClass=beanClass;
    }
    
//    protected void doParse(Element element, BeanDefinitionBuilder bean) {  
//        String id = element.getAttribute("id");
//        //interface是关键字，避免以下
//        String intf = element.getAttribute("interface");
//        String loadbalance = element.getAttribute("loadbalance");
//        String cluster = element.getAttribute("cluster");
//        String retries = element.getAttribute("retries");
//        if (StringUtils.hasText(id)) {  
//            bean.addPropertyValue("id", id);  
//        }  
//        if (StringUtils.hasText(intf)) {  
//            bean.addPropertyValue("interface", intf);  
//        }  
//        if (StringUtils.hasText(loadbalance)) {  
//            bean.addPropertyValue("loadbalance", loadbalance);  
//        }
//        
//        if (StringUtils.hasText(cluster)) {  
//            bean.addPropertyValue("cluster", cluster);  
//        }
//        
//        if (StringUtils.hasText(retries)) {  
//            bean.addPropertyValue("retries", Integer.valueOf(retries));  
//        }
//    }

	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		RootBeanDefinition beanDefinition=new RootBeanDefinition(); 
		//spring 会把这个beanClass进行实例化 beanDefinitionNames?
		beanDefinition.setBeanClass(beanClass);
		beanDefinition.setLazyInit(false);
		 //interface是关键字，避免以下
		String id=element.getAttribute("id");
        String intf = element.getAttribute("interface");
        String loadbalance = element.getAttribute("loadbalance");
        String cluster = element.getAttribute("cluster");
        String retries = element.getAttribute("retries");
        if(StringUtils.isEmpty(id)){
        	throw new RuntimeException("Reference id 不能为空！");
        }
        if(StringUtils.isEmpty(intf)){
        	throw new RuntimeException("Reference interface 不能为空！");
        }
        if(StringUtils.isEmpty(loadbalance)){
        	throw new RuntimeException("Reference loadbalance 不能为空！");
        }
        if(StringUtils.isEmpty(cluster)){
        	throw new RuntimeException("Reference cluster 不能为空！");
        }
        if(StringUtils.isEmpty(retries)){
        	throw new RuntimeException("Reference retries 不能为空！");
        }
        beanDefinition.getPropertyValues().addPropertyValue("id", id);
        beanDefinition.getPropertyValues().addPropertyValue("interface", intf);
        beanDefinition.getPropertyValues().addPropertyValue("loadbalance", loadbalance);
        beanDefinition.getPropertyValues().addPropertyValue("cluster", cluster);
        beanDefinition.getPropertyValues().addPropertyValue("retries", retries);
        
        parserContext.getRegistry().registerBeanDefinition("Reference"+id, beanDefinition);
		
		return beanDefinition;
	}
}