package com.itheima.schema;


import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class UserNamespaceHandler extends NamespaceHandlerSupport {
	
	public void init() {
		/***
		 *  user.xsd文件中 name="user"
		 *  解析user节点
		 */
		registerBeanDefinitionParser("user", new UserBeanDefinitionParser());
	}
}