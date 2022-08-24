package com.yida.controller;

import com.yida.service.OrderService;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/order")
public class OrderController {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	
	/**
	 * 订单服务接口
	 */
	@DubboReference(version = "${dubbo.spring.provider.version}", timeout = 1000, retries = 3, loadbalance = "roundrobin")
	private OrderService orderService;
	
	/**
	 * 获取订单详情接口
	 *
	 * @param orderId
	 * @return
	 */
	@RequestMapping("/getOrder")
	@ResponseBody
	public String getOrder(Long orderId) {
		String result = null;
		try {
			result = orderService.getOrder(orderId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}
	
}