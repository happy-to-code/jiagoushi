package com.yida.customer.aspect;

import com.yida.customer.annotation.RequestInitial;
import com.yida.customer.factory.CustomerParserFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
public class RequestInitialAspect {
	@Autowired
	private HttpServletRequest request;
	
	@Pointcut("@annotation(com.yida.customer.annotation.RequestInitial)")
	public void annotationPointCut() {
	}
	
	@Around("annotationPointCut()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		RequestInitial requestInitial = getRequestInitial(pjp);
		Object[] args = pjp.getArgs();
		
		if (null != requestInitial) {
			//拦截后给属性赋值
			CustomerParserFactory.initialDefValue(args, requestInitial);
		}
		
		return pjp.proceed(args);
	}
	
	private RequestInitial getRequestInitial(ProceedingJoinPoint pjp) {
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		
		//	获取方法
		Method method = signature.getMethod();
		// 获取方法上的注解
		return method.getAnnotation(RequestInitial.class);
	}
	
}
