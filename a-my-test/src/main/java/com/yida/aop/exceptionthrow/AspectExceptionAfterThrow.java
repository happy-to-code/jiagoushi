package com.yida.aop.exceptionthrow;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class AspectExceptionAfterThrow {
	
	@AfterThrowing(value = "execution(* com.yida.aop.common.ICalculatorAop.*(..))", throwing = "e")
	public void exceptionAfterThrow(JoinPoint joinPoint, Exception e) {
		String methodName = joinPoint.getSignature().getName();
		List<Object> list = Arrays.asList(joinPoint.getArgs());
		System.out.println("执行在" + methodName + "方法后的日志信息，方法参数为:" + list + "，异常信息为:" + e);
	}
}
