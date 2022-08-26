package com.yida.controller;

import com.yida.pojo.Student;
import com.yida.service.StudentService;
import com.yida.util.ApplicationContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/yida/")
public class StudentController {
	
	@Autowired
	private StudentService studentService;
	
	
	@GetMapping("getAllStudent")
	public List<Student> getAllStudent() {
		// 通过工具获取bean
		StudentService bean = ApplicationContextUtil.getBean(StudentService.class);
		List<Student> allUser = bean.findAllUser();
		System.out.println("allUser = " + allUser);
		
		return studentService.findAllUser();
	}
}
