package com.yida.service.impl;

import com.yida.service.OrderService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

@DubboService(version = "${dubbo.spring.provider.version}")
public class OrderServiceImpl implements OrderService {
	/**
	 * 服务端口
	 */
	@Value("${server.port}")
	private String serverPort;
	
	@Value("${dubbo.spring.provider.version}")
	private String serviceVersion;
	
	@Override
	public String getOrder(Long orderId) {
		String result = "get order detail ,orderId=" + orderId + ",serverPort=" + serverPort + ",serviceVersion=" + serviceVersion + "\t" + UUID.randomUUID().toString();
		System.out.println(result);
		return result;
	}
}
