package com.yida.aop.afterreturn;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Aspect
public class AspectAfterReturn {
	
	@AfterReturning(value = "execution(* com.yida.aop.common.ICalculatorAop.*(..))", returning = "result")
	public void afterReturn(JoinPoint joinPoint, Object result) {
		String name = joinPoint.getSignature().getName();
		List<Object> args = Arrays.asList(joinPoint.getArgs());
		
		System.out.println("执行在" + name + "方法[后]的日志信息，方法参数为:" + args + "\t 结果为：" + result);
	}
}
