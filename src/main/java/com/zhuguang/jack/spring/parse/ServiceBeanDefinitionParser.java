package com.zhuguang.jack.spring.parse;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import com.zhuguang.jack.configBean.Service;
import com.zhuguang.jack.utils.StringUtils;

public class ServiceBeanDefinitionParser implements BeanDefinitionParser {
	
	private final Class<?> beanClass;
    
    protected Class getBeanClass(Element element) {  
        return Service.class;  
    }
    
    public ServiceBeanDefinitionParser(Class<?> beanClass) {
		this.beanClass = beanClass;
	}

//	protected void doParse(Element element, BeanDefinitionBuilder bean) {  
//        String interface1 = element.getAttribute("interface");
//        String ref = element.getAttribute("ref");
//        String protocol = element.getAttribute("protocol");
//        if (StringUtils.hasText(interface1)) {  
//            bean.addPropertyValue("interface", interface1);  
//        }  
//        if (StringUtils.hasText(ref)) {  
//            bean.addPropertyValue("ref", ref);  
//        }  
//        if (StringUtils.hasText(protocol)) {  
//            bean.addPropertyValue("protocol", protocol);  
//        }
//    }

	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		RootBeanDefinition beanDefinition=new RootBeanDefinition();
		beanDefinition.setBeanClass(beanClass);
		beanDefinition.setLazyInit(false);
		String intf = element.getAttribute("interface");
        String ref = element.getAttribute("ref");
        String protocol = element.getAttribute("protocol");
		if(StringUtils.isEmpty(intf)){
			throw new RuntimeException("Service interface 不能为空！");
		}
		if(StringUtils.isEmpty(ref)){
			throw new RuntimeException("Service ref 不能为空！");
		}
		if(StringUtils.isEmpty(protocol)){
			throw new RuntimeException("Service protocol 不能为空！");
		}
		
		beanDefinition.getPropertyValues().addPropertyValue("interface",intf);
		beanDefinition.getPropertyValues().addPropertyValue("ref",ref);
		beanDefinition.getPropertyValues().addPropertyValue("protocol",protocol);
		parserContext.getRegistry().registerBeanDefinition("Service"+intf, beanDefinition);
		
		return beanDefinition;
	}
}