package com.yida.Demo;

import com.yida.pojo.Student;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Properties;

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
	
	/**
	 * 获取所有构造方法（包括私有的）
	 */
	@Test
	public void getAllConstructors() throws Exception {
		// 获取字节码文件
		Class<?> clazz = Class.forName("com.yida.pojo.Student");
		
		Constructor<?>[] constructors = clazz.getDeclaredConstructors();
		for (Constructor<?> constructor : constructors) {
			System.out.println("constructor = " + constructor);
		}
	}
	
	/**
	 * 获取成员变量并调用：
	 * <p>
	 * 1.批量的
	 * 1).Field[] getFields():获取所有的"公有字段"
	 * 2).Field[] getDeclaredFields():获取所有字段，包括：私有、受保护、默认、公有；
	 * 2.获取单个的：
	 * 1).public Field getField(String fieldName):获取某个"公有的"字段；
	 * 2).public Field getDeclaredField(String fieldName):获取某个字段(可以是私有的)
	 * <p>
	 * 设置字段的值：
	 * Field --> public void set(Object obj,Object value):
	 * 参数说明：
	 * 1.obj:要设置的字段所在的对象；
	 * 2.value:要为字段设置的值；
	 */
	@Test
	public void getAllFields() throws Exception {
		Class<?> clazz = Class.forName("com.yida.pojo.Student");
		// 1).Field[] getFields():获取所有的"公有字段"
		Field[] publicFields = clazz.getFields();
		for (Field publicField : publicFields) {
			System.out.println("共有字段 = " + publicField);
		}
		System.out.println("-----------------------------------------");
		// 2).Field[] getDeclaredFields():获取所有字段，包括：私有、受保护、默认、公有；
		Field[] allFields = clazz.getDeclaredFields();
		for (Field field : allFields) {
			System.out.println("获取所有字段 = " + field);
		}
		System.out.println("-----------------------------------------");
		// 1).public Field getField(String fieldName):获取某个"公有的"字段；
		Field age = clazz.getField("age");
		System.out.println("age = " + age);
		System.out.println("-----------------------------------------");
		// 2).public Field getDeclaredField(String fieldName):获取某个字段(可以是私有的)
		Field name = clazz.getDeclaredField("name");
		System.out.println("name = " + name);
		System.out.println("---------------------=========--------------------");
		// Field --> public void set(Object obj,Object value):
		Constructor<?> constructor = clazz.getConstructor(); // 获取构造器
		Object obj = constructor.newInstance(); // 通过构造器生成对象
		Field[] declaredFields = clazz.getDeclaredFields(); // 获取所有字段值
		for (Field field : declaredFields) {
			if (!field.isAccessible()) {
				field.setAccessible(true);
			}
			//	给对象设置字段值
			if (field.toString().contains("name")) {
				field.set(obj, "张三");
			}
			if (field.toString().contains("age")) {
				field.set(obj, 18);
			}
			if (field.toString().contains("address")) {
				field.set(obj, "上海");
			}
			if (field.toString().contains("gender")) {
				field.set(obj, 1);
			}
		}
		System.out.println("obj = " + obj);
	}
	
	/**
	 * 获取成员方法并调用：
	 */
	/*
		 * 获取成员方法并调用：
		 *
		 * 1.批量的：
		 * 		public Method[] getMethods():获取所有"公有方法"；（包含了父类的方法也包含Object类）
		 * 		public Method[] getDeclaredMethods():获取所有的成员方法，包括私有的(不包括继承的)
		 * 2.获取单个的：
		 * 		public Method getMethod(String name,Class<?>... parameterTypes):
		 * 					参数：
		 * 						name : 方法名；
		 * 						Class ... : 形参的Class类型对象
		 * 		public Method getDeclaredMethod(String name,Class<?>... parameterTypes)
		 *
		 * 	 调用方法：
		 * 		Method --> public Object invoke(Object obj,Object... args):
		 * 					参数说明：
		 * 					obj : 要调用方法的对象；
		 * 					args:调用方式时所传递的实参；
		):
    */
	@Test
	public void testMethod() throws Exception {
		Class<?> clazz = Class.forName("com.yida.pojo.Student");
		// public Method[] getMethods():获取所有"公有方法"；（包含了父类的方法也包含Object类）
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			System.out.println("共有方法 = " + method);
		}
		System.out.println("-----------------------------------------------");
		Method[] declaredMethods = clazz.getDeclaredMethods();
		for (Method declaredMethod : declaredMethods) {
			System.out.println("获取所有的方法，包括私有的 = " + declaredMethod);
		}
		System.out.println("-----------------------------------------------");
		Method show1 = clazz.getDeclaredMethod("show1", String.class); // 获取方法
		Constructor<?> constructor = clazz.getConstructor(); // 获取构造器
		Object obj = constructor.newInstance();// 通过构造器生成对象
		show1.invoke(obj, "李四"); // 方法调用   此处是反射，所以使用 方法.invoke(对象，参数)
		
		
		System.out.println("-----------------------------------------------");
		Method show4 = clazz.getDeclaredMethod("show4", int.class); // show4是私有方法
		System.out.println("show4 = " + show4);
		show4.setAccessible(true);// 解除私有限定
		Object result = show4.invoke(obj, 20);//需要两个参数，一个是要调用的对象（获取有反射），一个是实参
		System.out.println("show4 返回值：" + result);
	}
	
	/**
	 * 反射方法的其他使用--通过反射越过泛型检查
	 */
	@Test
	public void testAddItem() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		ArrayList<String> strList = new ArrayList<>();
		strList.add("aaa");
		strList.add("bbb");
		
		//	strList.add(100);
		//  获取ArrayList的Class对象，反向的调用add()方法，添加数据
		Class listClass = strList.getClass(); // 得到 strList 对象的字节码 对象
		// 获取add()方法
		Method m = listClass.getMethod("add", Object.class);
		//调用add()方法
		m.invoke(strList, 100);
		m.invoke(strList, true);
		m.invoke(strList, 3.1415926);
		
		//遍历集合
		for (Object obj : strList) {
			System.out.println(obj);
		}
	}
	
	/**
	 * 反射方法的其他使用--通过反射运行配置文件内容：
	 * <p>
	 * 配置文件以pro.txt文件为例子：
	 * className = cn.fanshe.Student
	 * methodName = show
	 */
	public static String getValue(String key) throws IOException {
		Properties pro = new Properties();//获取配置文件的对象
		FileReader in = new FileReader("pro.txt");//获取输入流
		pro.load(in);//将流加载到配置文件对象中
		in.close();
		return pro.getProperty(key);//返回根据key获取的value值
	}
	
	@Test
	public void run() throws Exception {
		// 通过反射获取Class对象
		Class stuClass = Class.forName(getValue("className"));// className 是配置文件的key   cn.fanshe.Student
		// 2.获取show()方法
		Method m = stuClass.getMethod(getValue("methodName"));// methodName是配置文件的key    show
		// 3.调用show()方法
		m.invoke(stuClass.getConstructor().newInstance());
	}
	/**
	 * 需求：
	 *
	 * 当我们升级这个系统时，不要Student类，而需要新写一个Student2的类时，这时只需要更改pro.txt的文件内容就可以了。代码就一点不用改动。
	 * 配置文件更改为：
	 * className = cn.fanshe.Student2
	 * methodName = show2
	 */
	
}
