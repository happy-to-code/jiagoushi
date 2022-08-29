package com.yida.driver.service;

import com.yida.driver.model.Driver;

public interface DriverService {
	Driver findById(String id);
	
	void update(String id, Integer status);
}
