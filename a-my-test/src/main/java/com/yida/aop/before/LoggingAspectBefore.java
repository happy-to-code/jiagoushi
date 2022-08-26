package com.yida.aop.before;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author ：zhangyifei
 * @date ：Created in 2022/8/26 17:16
 * @description：
 * @modified By：
 * @version:
 */
@Component
@Aspect
public class LoggingAspectBefore {
	
	/**
	 * com.yida.aop.common.impl.CalculatorAop.*(..)
	 * 包括CalculatorAop下所有的方法
	 *
	 * @param joinPoint
	 */
	@Before(value = "execution(* com.yida.aop.common.ICalculatorAop.*(..))")
	public void before(JoinPoint joinPoint) {
		String methodName = joinPoint.getSignature().getName();
		
		List<Object> params = Arrays.asList(joinPoint.getArgs());
		
		System.out.println("执行在:" + methodName + "前的方法,参数params = " + params);
	}
}
