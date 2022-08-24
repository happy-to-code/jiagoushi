package com.yida.order.service.impl;

import com.yida.order.mapper.OrderInfoMapper;
import com.yida.order.model.OrderInfo;
import com.yida.order.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderInfoServiceImpl implements OrderInfoService {
	
	@Autowired
	private OrderInfoMapper orderInfoMapper;
	
	@Override
	public void add(OrderInfo orderInfo) {
		orderInfoMapper.add(orderInfo);
	}
}
