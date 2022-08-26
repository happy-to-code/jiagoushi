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
		int add = iCalculatorAop.add(1, 3);
		System.out.println("add = " + add);
	}
}