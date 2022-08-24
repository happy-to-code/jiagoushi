package com.yida.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractNameValueGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class MyGatewayFilterFactory extends AbstractNameValueGatewayFilterFactory {
	@Override
	public GatewayFilter apply(NameValueConfig config) {
		return new GatewayFilter() {
			@Override
			public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
				List<String> tokens = exchange.getRequest().getHeaders().get(config.getName());
				if (tokens == null || tokens.size() > 1) {
					exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
					return exchange.getResponse().setComplete();
				}
				String token = tokens.get(0);
				// parse token 具体业务具体对待
				if (!token.equals(config.getValue())) {
					exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
					return exchange.getResponse().setComplete();
				}
				return chain.filter(exchange);
			}
		};
	}
}
