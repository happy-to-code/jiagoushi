package com.yida.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfiguration {
	
	
	//@Bean
	public RouteLocator routeLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("hailtaxi-driver", r -> r.path("/driver/**").uri("lb://hailtaxi-driver"))
				.route("hailtaxi-order", r -> r.path("/order/**").uri("lb://hailtaxi-order"))
				.build();
	}
	
	
}
