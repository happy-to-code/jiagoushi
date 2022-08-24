package com.yida.service.impl;

import com.yida.pojo.Student;
import com.yida.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
	@Override
	public List<Student> findAllUser() {
		List<Student> students = new ArrayList<>();
		Student s = new Student();
		s.setAge(13);
		s.setName("小明");
		s.setAddress("北京");
		s.setGender(1);
		
		students.add(s);
		return students;
	}
}
