package com.yida.aop.around;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 环绕通知的功能比较强大，它能够通过一个方法实现之前的所有通知效果
 */
@Component
@Aspect
public class AspectAround {
	
	@Around(value = "execution(* com.yida.aop.common.ICalculatorAop.*(..))")
	public Object aroundMethod(ProceedingJoinPoint proceedingJoinPoint) {
		Object result = null; // 返回值
		String methodName = proceedingJoinPoint.getSignature().getName();
		List<Object> args = Arrays.asList(proceedingJoinPoint.getArgs());
		try {
			
			//	前置通知
			System.out.println(methodName + "【方法前】args = " + args);
			
			//	执行目标方法
			result = proceedingJoinPoint.proceed();
			//	返回通知
			System.out.println(methodName + "【方法后--返回通知】args = " + args + " 执行结果：" + result);
		} catch (Throwable e) {
			//	异常通知
			System.out.println(methodName + "【方法后--异常通知】args = " + args + "  异常信息:" + e);
		}
		//	后置通知
		System.out.println(methodName + "【方法后--后置通知】args = " + args);
		
		// 返回结果
		return result;
	}
}
