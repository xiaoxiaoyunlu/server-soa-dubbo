package com.zhuguang.jack.spring.parse;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.zhuguang.jack.configBean.Registry;
import com.zhuguang.jack.utils.StringUtils;

public class RegistryBeanDefinitionParser implements BeanDefinitionParser {
	
	private final Class<?> beanClass;
    
    public RegistryBeanDefinitionParser(Class<?> beanClass) {
    	this.beanClass=beanClass;
	}

	protected Class getBeanClass(Element element) {  
        return Registry.class;  
    }

	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		RootBeanDefinition beanDefinition=new RootBeanDefinition(); 
		//spring 会把这个beanClass进行实例化 beanDefinitionNames?
		beanDefinition.setBeanClass(beanClass);
		beanDefinition.setLazyInit(false);
		String protocol = element.getAttribute("protocol");
        String address = element.getAttribute("address");
        if(StringUtils.isEmpty(protocol)){
        	throw new RuntimeException("Registry protocol 不能为空！");
        }
        if(StringUtils.isEmpty(address)){
        	throw new RuntimeException("Registry address 不能为空！");
        }
        beanDefinition.getPropertyValues().addPropertyValue("protocol", protocol);
        beanDefinition.getPropertyValues().addPropertyValue("address", address);
        parserContext.getRegistry().registerBeanDefinition("Registry"+address, beanDefinition);
		return null;
	}
}