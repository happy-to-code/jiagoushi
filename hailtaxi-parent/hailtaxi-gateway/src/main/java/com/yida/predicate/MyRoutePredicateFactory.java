package com.yida.predicate;

import lombok.Data;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.GatewayPredicate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Component
public class MyRoutePredicateFactory extends AbstractRoutePredicateFactory<MyRoutePredicateFactory.Config> {
	
	
	/**
	 * Name key.
	 */
	public static final String NAME_KEY = "name";
	
	/**
	 * Regexp key.
	 */
	public static final String VALUE_KEY = "value";
	
	public MyRoutePredicateFactory() {
		super(Config.class);
	}
	
	@Override
	public List<String> shortcutFieldOrder() {
		return Arrays.asList(NAME_KEY, VALUE_KEY);
	}
	
	
	@Override
	public Predicate<ServerWebExchange> apply(Config config) {
		return new GatewayPredicate() {
			@Override
			public boolean test(ServerWebExchange serverWebExchange) {
				System.out.println("MyRoutePredicateFactory-----");
				List<String> headers = serverWebExchange.getRequest().getHeaders().get(config.name);
				if (headers == null || headers.size() > 1) {
					return false;
				}
				String header = headers.get(0);
				if (header.equals(config.value)) {
					return true;
				}
				return false;
			}
		};
	}
	
	
	@Data
	public static class Config {
		private String name;
		private String value;
	}
}
