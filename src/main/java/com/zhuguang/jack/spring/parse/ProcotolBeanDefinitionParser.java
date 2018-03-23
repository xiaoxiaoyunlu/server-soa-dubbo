package com.zhuguang.jack.spring.parse;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.zhuguang.jack.configBean.Protocol;
import com.zhuguang.jack.utils.StringUtils;

public class ProcotolBeanDefinitionParser implements BeanDefinitionParser {
	
	private final Class<?> beanClass;
    
    protected Class getBeanClass(Element element) {  
        return Protocol.class;  
    }
    
    public ProcotolBeanDefinitionParser(Class<?> beanClass) {
		this.beanClass = beanClass;
	}

	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		RootBeanDefinition beanDefinition=new RootBeanDefinition();
		beanDefinition.setBeanClass(beanClass);
		beanDefinition.setLazyInit(false);
		String name = element.getAttribute("name");
        String address = element.getAttribute("address");
        String port = element.getAttribute("port");
        String host = element.getAttribute("host");
        
        if(StringUtils.isEmpty(name)){
        	throw new RuntimeException("Protocol name 不能为空！");
        }
        if(StringUtils.isEmpty(address)){
        	throw new RuntimeException("Protocol address 不能为空！");
        }
        if(StringUtils.isEmpty(port)){
        	throw new RuntimeException("Protocol port 不能为空！");
        }
        if(StringUtils.isEmpty(host)){
        	throw new RuntimeException("Protocol host 不能为空！");
        }
        
        beanDefinition.getPropertyValues().addPropertyValue("name", name);
        beanDefinition.getPropertyValues().addPropertyValue("address", address);
        beanDefinition.getPropertyValues().addPropertyValue("port", port);
        beanDefinition.getPropertyValues().addPropertyValue("host", host);
        parserContext.getRegistry().registerBeanDefinition("Protocol"+name, beanDefinition);
		return beanDefinition;
	}
}