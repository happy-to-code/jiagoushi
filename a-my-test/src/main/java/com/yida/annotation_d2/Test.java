package com.yida.annotation_d2;

import java.lang.reflect.Field;

public class Test {
	public static void main(String[] args) throws NoSuchFieldException {
		Class<Hello> helloClass = Hello.class;
		
		// 判断Hello类上是否有Report注解
		boolean helloClassAnnotationPresent = helloClass.isAnnotationPresent(Report.class);
		System.out.println("helloClassAnnotationPresent = " + helloClassAnnotationPresent);
		
		// 获取名为name的字段
		Field nameField = helloClass.getDeclaredField("name");
		System.out.println("nameField = " + nameField);
		// 判断name字段上是否有Report注解
		boolean nameFieldAnnotationPresent = nameField.isAnnotationPresent(Report.class);
		System.out.println("nameFieldAnnotationPresent = " + nameFieldAnnotationPresent);
		
		Field ageField = helloClass.getDeclaredField("age");
		boolean ageFieldAnnotationPresent = ageField.isAnnotationPresent(Report.class);
		System.out.println("ageFieldAnnotationPresent = " + ageFieldAnnotationPresent);
		System.out.println("---------------------------------------------------------------");
		
		Report report = helloClass.getAnnotation(Report.class);
		System.out.println(report.value() + "\t" + report.level() + "\t" + report.type());
		Report report1 = nameField.getAnnotation(Report.class);
		System.out.println(report1.value() + "\t" + report1.level() + "\t" + report1.type());
	}
}
