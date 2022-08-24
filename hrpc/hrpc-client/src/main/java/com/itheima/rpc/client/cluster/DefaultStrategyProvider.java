package com.itheima.rpc.client.cluster;

import com.itheima.rpc.annotation.HrpcLoadBalance;
import com.itheima.rpc.client.config.RpcClientConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DefaultStrategyProvider implements StartegyProvider, ApplicationContextAware {
	
	@Autowired
	private RpcClientConfiguration clientConfiguration;
	
	private LoadBalanceStrategy loadBalanceStrategy;
	
	@Override
	public LoadBalanceStrategy getStrategy() {
		return loadBalanceStrategy;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(HrpcLoadBalance.class);
		for (Object bean : beansWithAnnotation.values()) {
			HrpcLoadBalance loadBalance = bean.getClass().getAnnotation(HrpcLoadBalance.class);
			if (loadBalance.strategy().equals(clientConfiguration.getRpcClientClusterStrategy())) {
				loadBalanceStrategy = (LoadBalanceStrategy) bean;
				break;
			}
		}
		
	}
}
