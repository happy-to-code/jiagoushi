package com.yida.driver.feign.interceptor;

import feign.Request;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 * 1、直接实现接口
 * 2、继承 BaseRequestInterceptor
 */
@Slf4j
public class MyRequestInterceptor implements RequestInterceptor {
	@Override
	public void apply(RequestTemplate template) {
		String url = template.url();
		Map<String, Collection<String>> headers = template.headers();
		String method = template.method();
		Map<String, Collection<String>> queries = template.queries();
		Request.Body body = template.requestBody();
		log.info("url={},headers={},method={},queries={},body={}", url, headers, method, queries, body);
		
		//添加头信息
		template.header("GlobalId", UUID.randomUUID().toString());
	}
}
