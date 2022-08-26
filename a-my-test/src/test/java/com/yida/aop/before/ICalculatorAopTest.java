package com.yida.aop.before;

import com.yida.aop.common.ICalculatorAop;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author ：zhangyifei
 * @date ：Created in 2022/8/26 17:44
 * @description：
 * @modified By：
 * @version:
 */
@SpringBootTest
class ICalculatorAopTest {
	
	@Autowired
	private ICalculatorAop iCalculatorAop;
	
	@Test
	void add() {
		// iCalculatorAop.add(1, 3);
		int divide = iCalculatorAop.divide(15, 5);
		System.out.println("divide = " + divide);
		// int sub = iCalculatorAop.subtract(5, 3);
		// System.out.println("sub = " + sub);
		// int multiply = iCalculatorAop.multiply(2, 8);
		// System.out.println("multiply = " + multiply);
		// int divide = iCalculatorAop.divide(10, 5);
		// System.out.println("divide = " + divide);
	}
}