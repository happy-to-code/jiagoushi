package com.yida.Demo;

import com.yida.pojo.Student;
import org.junit.Test;

import java.lang.reflect.Constructor;

public class fanshe {
	
	/**
	 * 获取对象的字节码
	 * （1）Object-->getClass
	 * （2）任何数据类型（包括基本的数据类型）都有一个“静态”的class属性
	 * （3）通过class类的静态方法：forName(String className)（最常用）
	 */
	@Test
	public void getClazz() throws ClassNotFoundException {
		// （1）Object-->getClass
		Student s = new Student();
		Class<? extends Student> clazz1 = s.getClass();
		System.out.println("clazz1 = " + clazz1);
		
		// （2）任何数据类型（包括基本的数据类型）都有一个“静态”的class属性
		Class<Student> clazz2 = Student.class;
		System.out.println("clazz2 = " + clazz2);
		
		// （3）通过class类的静态方法：forName(String className)（最常用）
		Class<?> clazz3 = Class.forName("com.yida.pojo.Student");
		System.out.println("clazz3 = " + clazz3);
		
		
		// 通过3种方法获取的字节码对象是同一个
		System.out.println(clazz1 == clazz2);
		System.out.println(clazz3 == clazz2);
	}
	
	/**
	 * 创建实例：通过反射来生成对象
	 * （1）使用Class对象的newInstance()方法来创建Class对象对应类的实例。
	 * （2）先通过Class对象获取指定的Constructor对象，再调用Constructor对象的newInstance()方法来创建对象，
	 * 这种方法可以用指定的构造器构造类的实例。
	 */
	@Test
	public void createInstance() throws Exception {
		// 获取字节码文件
		Class<?> clazz = Class.forName("com.yida.pojo.Student");
		
		// （1）使用Class对象的newInstance()方法来创建Class对象对应类的实例。
		Object o = clazz.newInstance();
		Student student = (Student) o;
		student.sayHello();
		
		//	==========================================================================
		// 2）先通过Class对象获取指定的Constructor对象，再调用Constructor对象的newInstance()方法来创建对象，
		// 这种方法可以用指定的构造器构造类的实例。
		Constructor<?> constructor1 = clazz.getConstructor(); // 声明的是空参构造方法
		Object o1 = constructor1.newInstance();
		Student student1 = (Student) o1;
		student1.sayHello();
		
		Constructor<?> constructor2 = clazz.getConstructor(String.class); // 声明一个有参构造方法  参数是字符串
		Object o2 = constructor2.newInstance("小花");
		Student student2 = (Student) o2;
		student2.sayHello();
		
		Constructor<?> constructor3 = clazz.getDeclaredConstructor(Integer.class); // 私有构造方法
		constructor3.setAccessible(true);  // 此处不加会报错  这边的作用是忽略掉访问修饰符  暴力访问
		Object o3 = constructor3.newInstance(17);
		Student student3 = (Student) o3;
		student3.sayHello();
	}
	
	
}
