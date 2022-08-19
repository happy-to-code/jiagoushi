package com.itheima.shop.order.service;

import com.itheima.rpc.annotation.HrpcService;
import com.itheima.rpc.server.config.RpcServerConfiguration;
import com.itheima.shop.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;
import java.util.UUID;

/**
 * @description
 * @author: ts
 * @create:2021-05-10 10:57
 */

@HrpcService(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private RpcServerConfiguration serverConfiguration;
	
	@Override
	public String getOrder(String userId, String orderNo) {
		return serverConfiguration.getServerPort() + "---" + serverConfiguration.getRpcPort() + "---Congratulations, The RPC call succeeded,orderNo is " + orderNo + ",userId is " + userId + "\t " + UUID.randomUUID();
	}
}
