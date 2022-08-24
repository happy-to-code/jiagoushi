package com.yida.service;

import com.yida.pojo.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class StudentServiceTest {
	@Autowired
	private StudentService studentService;
	
	@Test
	public void findAllUser() {
		List<Student> students = studentService.findAllUser();
		System.out.println("students = " + students);
	}
}