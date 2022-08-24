package com.yida.driver.feign.fallback;


import com.yida.driver.feign.DriverFeign;
import com.yida.driver.model.Driver;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class DriverFeignFallBackFactory implements FallbackFactory<DriverFeign> {
	@Override
	public DriverFeign create(Throwable cause) {
		return new DriverFeign() {
			@Override
			public Driver status(String id, Integer status) {
				System.out.println("走降级");
				return new Driver();
			}
		};
	}
}
