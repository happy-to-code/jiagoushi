package com.yida.pojo.annotation;

@MyAnnotation(
		name="Jakob",
		age=37,
		newNames={"Jenkov", "Peterson"}
)
public class MyClassDemo {
	public static void main(String[] args) {
		MyClassDemo myClassDemo = new MyClassDemo();
		System.out.println("myClassDemo = " + myClassDemo);
		
		Class<? extends MyClassDemo> clazz = myClassDemo.getClass();
		MyAnnotation annotation = clazz.getAnnotation(MyAnnotation.class);
		System.out.println("annotation = " + annotation);
		System.out.println("annotation.value() = " + annotation.value());
		System.out.println("annotation.name() = " + annotation.name());
		System.out.println("annotation.age() = " + annotation.age());
		
		for (String newName : annotation.newNames()) {
			System.out.println("newName = " + newName);
		}
	}
}
