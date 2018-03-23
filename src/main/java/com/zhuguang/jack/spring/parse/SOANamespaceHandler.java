package com.zhuguang.jack.spring.parse;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import com.zhuguang.jack.configBean.Protocol;
import com.zhuguang.jack.configBean.Reference;
import com.zhuguang.jack.configBean.Registry;
import com.zhuguang.jack.configBean.Service;

public class SOANamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		
		registerBeanDefinitionParser("registry",new RegistryBeanDefinitionParser(Registry.class));
		registerBeanDefinitionParser("protocol",new ProcotolBeanDefinitionParser(Protocol.class));
		registerBeanDefinitionParser("service",new RegistryBeanDefinitionParser(Service.class));
		registerBeanDefinitionParser("reference",new RegistryBeanDefinitionParser(Reference.class));

	}

}
