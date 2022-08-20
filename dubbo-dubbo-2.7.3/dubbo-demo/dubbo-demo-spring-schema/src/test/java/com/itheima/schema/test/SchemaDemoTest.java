package com.itheima.schema.test;

import com.itheima.schema.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SchemaDemoTest {
	
	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring/applicationContext.xml");
		User user = (User) ctx.getBean("user");
		System.out.println(user);
		
		//for (String beanDefinitionName : ctx.getBeanDefinitionNames()) {
		//	System.out.println(beanDefinitionName);
		//}
	}
}
