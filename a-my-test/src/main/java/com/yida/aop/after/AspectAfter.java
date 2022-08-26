package com.yida.aop.after;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Aspect
public class AspectAfter {
	
	@After("execution(* com.yida.aop.common.ICalculatorAop.*(..))")
	public void after(JoinPoint joinPoint){
		String name = joinPoint.getSignature().getName();
		List<Object> args = Arrays.asList(joinPoint.getArgs());
		
		System.out.println("执行在" + name + "方法后的日志信息，方法参数为:" + args);
	}
}
