package com.yida.aop.before;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
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

	@Before(value = "execution(* com.yida.aop.before.impl.*.*(..))")
	public void beforeMethod(JoinPoint joinPoint) {
		String methodName = joinPoint.getSignature().getName();

		List<Object> params = Arrays.asList(joinPoint.getArgs());

		System.out.println("执行在:" + methodName + "的方法,参数params = " + params);
	}
}
