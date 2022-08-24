package com.yida.config;

import org.apache.dubbo.config.ProviderConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 设置超时时间
 */
@Configuration
public class DubboCustomerConfig {
	
	@Bean
	public ProviderConfig registryConfig() {
		ProviderConfig providerConfig = new ProviderConfig();
		providerConfig.setTimeout(1000);
		
		return providerConfig;
	}
}
