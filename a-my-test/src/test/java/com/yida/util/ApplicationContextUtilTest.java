package com.yida.util;

import com.yida.pojo.Student;
import com.yida.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ApplicationContextUtilTest {
	@Autowired
	private ApplicationContextUtil applicationContextUtil;
	
	
	@Test
	public void testApplicationContextUtil() {
		StudentService bean = ApplicationContextUtil.getBean(StudentService.class);
		System.out.println("bean = " + bean);
	}
	
	@Test
	public void getBean() {
		StudentService bean = applicationContextUtil.getBean(StudentService.class);
		System.out.println("bean = " + bean);
		List<Student> allUser = bean.findAllUser();
		System.out.println("allUser = " + allUser);
	}
}
