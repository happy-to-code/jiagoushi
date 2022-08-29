package com.yida.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class RouterFilter implements GlobalFilter, Ordered {
	
	/***
	 * 路由拦截
	 * @param exchange
	 * @param chain
	 * @return
	 */
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		log.info("---------RouterFilter----------------");
		//获取请求参数
		String token = exchange.getRequest().getQueryParams().getFirst("token");
		System.out.println("token = " + token);
		//如果token为空，则表示没有登录
		if (StringUtils.isEmpty(token)) {
			//没登录,状态设置413
			exchange.getResponse().setStatusCode(HttpStatus.PAYLOAD_TOO_LARGE);
			//结束请求
			return exchange.getResponse().setComplete();
		}
		//放行
		return chain.filter(exchange);
	}
	
	/***
	 * 拦截器顺序
	 * @return
	 */
	@Override
	public int getOrder() {
		return 0;
	}
}
