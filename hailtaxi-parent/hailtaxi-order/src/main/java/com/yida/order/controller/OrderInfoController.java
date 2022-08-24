package com.yida.order.controller;

import com.yida.driver.feign.DriverFeign;
import com.yida.driver.model.Driver;
import com.yida.order.model.OrderInfo;
import com.yida.order.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(value = "/order")
public class OrderInfoController {
	
	
	@Autowired
	private OrderInfoService orderInfoService;
	
	@Autowired
	private DriverFeign driverFeign;
	
	
	/***
	 * 下单
	 */
	@PostMapping
	public OrderInfo add() {
		
		//修改司机信息  司机ID=1
		Driver driver = driverFeign.status("1", 2);
		//创建订单
		OrderInfo orderInfo = new OrderInfo("No" + ((int) (Math.random() * 10000)), (int) (Math.random() * 100), new Date(), "深圳北站", "罗湖港", driver);
		orderInfoService.add(orderInfo);
		return orderInfo;
	}
	
}
