package com.itheima.rpc.client.proxy;

import com.itheima.rpc.client.request.RpcRequestManager;
import com.itheima.rpc.data.RpcRequest;
import com.itheima.rpc.data.RpcResponse;
import com.itheima.rpc.exception.RpcException;
import com.itheima.rpc.spring.SpringBeanFactory;
import com.itheima.rpc.util.RequestIdUtil;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * @description
 * @author: ts
 * @create:2021-05-12 00:11
 */
public class CglibProxyCallBackHandler implements MethodInterceptor {
	
	@Autowired
	private SpringBeanFactory springBeanFactory;
	
	public Object intercept(Object o, Method method, Object[] parameters, MethodProxy methodProxy) throws Throwable {
		// 放过toString、hashcode、equals 等方法   采用spring工具类判断
		if (ReflectionUtils.isHashCodeMethod(method)) {
			return method.invoke(method.getDeclaringClass().newInstance(), parameters);
		}
		
		// 发起远程调用
		// 1、构建请求
		// 1.1 生成一个requestId
		String requestId = RequestIdUtil.requestId();
		String serviceName = method.getDeclaringClass().getName(); // 服务的名称  com.itheima.order.OrderService
		String methodName = method.getName();
		Class<?>[] parameterTypes = method.getParameterTypes();// 方法参数类型
		
		RpcRequest rpcRequest = RpcRequest.builder().requestId(requestId).className(serviceName).methodName(methodName).parameterTypes(parameterTypes).parameters(parameters).build();
		// 发送请求并获取响应  todo 测试 @autowired 注入RpcRequestManager
		RpcRequestManager rpcRequestManager = SpringBeanFactory.getBean(RpcRequestManager.class);
		if (rpcRequestManager == null) {
			throw new RpcException("获取远程调用rpcRequestManager出错");
		}
		
		// 通过Netty服务器 发起远程调用
		RpcResponse response = rpcRequestManager.sendRequest(rpcRequest);
		return response.getResult();
	}
}
