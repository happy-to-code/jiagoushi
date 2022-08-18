package com.itheima.rpc.client.discovery.zk;

import com.itheima.rpc.cache.ServiceProviderCache;
import com.itheima.rpc.client.discovery.RpcServiceDiscovery;
import com.itheima.rpc.provider.ServiceProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ZKServiceDiscovery implements RpcServiceDiscovery {
	@Autowired
	private ClientZKit clientZKit;
	
	@Autowired
	private ServiceProviderCache providerCache; // 缓存
	
	@Override
	public void serviceDiscovery() {
		//	拉取接口列表
		List<String> serviceList = clientZKit.getServiceList();
		if (serviceList != null && serviceList.size() > 0) {
			for (String serviceName : serviceList) {
				//	获取该服务下所有提供者信息
				List<ServiceProvider> serviceInfos = clientZKit.getServiceInfos(serviceName);
				
				//	存入缓存
				providerCache.put(serviceName, serviceInfos);
				
				//	监听该服务下所有节点是否有信息变化
				clientZKit.subscribeZKEvent(serviceName);
				
				log.info("从zk加载的服务信息为,serviceName={},serviceInfos={}", serviceName, serviceInfos);
			}
		}
		
	}
}
