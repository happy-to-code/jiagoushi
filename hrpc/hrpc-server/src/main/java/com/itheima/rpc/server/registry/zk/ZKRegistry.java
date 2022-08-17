package com.itheima.rpc.server.registry.zk;

import com.itheima.rpc.annotation.HrpcService;
import com.itheima.rpc.server.config.RpcServerConfiguration;
import com.itheima.rpc.server.registry.RpcRegistry;
import com.itheima.rpc.spring.SpringBeanFactory;
import com.itheima.rpc.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class ZKRegistry implements RpcRegistry {
	@Autowired
	private ServerZKit zKit;
	
	@Autowired
	private RpcServerConfiguration serverConfiguration;
	
	/**
	 * 此处不注入会报错（虽然用的是静态方法）
	 * 要把bean注入容器
	 */
	@Autowired
	private SpringBeanFactory beanFactory;
	
	/**
	 * 基于zookeeper完成服务信息的注册
	 * 扫描业务代码中要注册的接口,将接口等信息写到注册中心
	 * 此处是把添加了@HrpcService注解的方法添加到注册中心
	 *
	 * @see HrpcService
	 */
	@Override
	public void serviceRegistry() {
		Map<String, Object> beanListByAnnotationClass = SpringBeanFactory.getBeanListByAnnotationClass(HrpcService.class);
		
		if (beanListByAnnotationClass != null && !beanListByAnnotationClass.isEmpty()) {
			//	通过zKit创建一个根节点
			zKit.createRootNode();
			
			//	获取ip
			String ip = IpUtil.getRealIp();
			
			for (Object bean : beanListByAnnotationClass.values()) {
				// @HrpcService(interfaceClass = OrderService.class)
				HrpcService hrpcService = bean.getClass().getAnnotation(HrpcService.class);
				
				//	接口服务
				Class<?> interfaceClass = hrpcService.interfaceClass();
				// com.itheima.order.OrderService
				String serviceName = interfaceClass.getName();
				
				//	通过在Kit创建一个节点（该节点在上面创建的根节点下面）
				zKit.createPersistentNode(serviceName);
				
				//	通过zKit在 接口节点下创建一个子节点；子节点包括提供者的 ip和端口等信息
				String childNode = serviceName + "/" + ip + ":" + serverConfiguration.getRpcPort();
				zKit.createNode(childNode); // 此时创建的是临时节点，通讯断开，节点销毁
				
				
				log.info("服务注册成功,serviceName:{},childNode:{}", serviceName, childNode);
			}
		}
	}
}
