package com.itheima.rpc.client.proxy;

import com.itheima.rpc.data.RpcRequest;
import com.itheima.rpc.data.RpcResponse;
import com.itheima.rpc.exception.RpcException;
import com.itheima.rpc.spring.SpringBeanFactory;
import com.itheima.rpc.util.RequestIdUtil;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * @description
 * @author: ts
 * @create:2021-05-12 00:11
 */
public class CglibProxyCallBackHandler implements MethodInterceptor {


    public Object intercept(Object o, Method method, Object[] parameters, MethodProxy methodProxy) throws Throwable {
        
        return null;
    }
}
