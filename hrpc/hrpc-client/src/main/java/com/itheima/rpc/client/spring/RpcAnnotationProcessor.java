package com.itheima.rpc.client.spring;

import com.itheima.rpc.annotation.HrpcRemote;
import com.itheima.rpc.proxy.ProxyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * 动态代理
 * 客户端controller中如果有HrpcRemote注解，需要创建代理,通过自定义RpcAnnotationProcessor
 * <p>
 * 在hrpc-client模块中编写RpcAnnotationProcessor，
 * 在postProcessAfterInitialization方法中去检测如果bean的某个属性上有HrpcRemote注解，则创建代理，并完成注入
 */
@Component
@Slf4j
public class RpcAnnotationProcessor implements BeanPostProcessor, ApplicationContextAware {
	
	@Autowired
	private ProxyFactory proxyFactory;
	
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		Field[] fields = bean.getClass().getDeclaredFields();
		for (Field field : fields) {
			try {
				if (!field.isAccessible()) { // 如果是私有的
					field.setAccessible(true);
				}
				//	检查bean的字段上是否有我们自定义的注解 @HrpcRemote
				HrpcRemote hrpcRemote = field.getAnnotation(HrpcRemote.class);
				if (hrpcRemote != null) {
					//	为该字段生成代理，并注入到该field上
					Class<?> type = field.getType();
					//	从容器中拿到代理工厂  生成代理
					Object proxy = proxyFactory.newProxyInstance(type);
					// log.info("客户端：{}生成的代理为：{}", field.getName(), proxy.toString());
					if (proxy != null) {
						field.set(bean, proxy);
					}
				}
			} catch (Exception e) {
				log.error("初始远程代理服务失败,field:{},class:{},原因：{}", field.getName(), bean.getClass().getName(), e.getMessage());
			}
		}
		
		return bean;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		//proxyFactory = applicationContext.getBean(ProxyFactory.class);
	}
}
