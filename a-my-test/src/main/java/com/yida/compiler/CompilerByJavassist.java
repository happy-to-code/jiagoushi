package com.yida.compiler;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;

/**
 * Javassist是一个开源的分析、编辑和创建Java字节码的类库
 * 能动态改变类的结构，或者动态生成类
 */
public class CompilerByJavassist {
	
	public static void main(String[] args) throws Exception {
		// ClassPool：class对象容器
		ClassPool pool = ClassPool.getDefault();
		
		// 通过ClassPool生成一个User类
		CtClass ctClass = pool.makeClass("com.itheima.domain.User");
		
		// 添加属性     -- private String username
		CtField enameField = new CtField(pool.getCtClass("java.lang.String"), "username", ctClass);
		
		enameField.setModifiers(Modifier.PRIVATE);
		
		ctClass.addField(enameField);
		
		// 添加属性    -- private int age
		CtField enoField = new CtField(pool.getCtClass("int"), "age", ctClass);
		
		enoField.setModifiers(Modifier.PRIVATE);
		
		ctClass.addField(enoField);
		
		//添加方法
		ctClass.addMethod(CtNewMethod.getter("getUsername", enameField));
		ctClass.addMethod(CtNewMethod.setter("setUsername", enameField));
		ctClass.addMethod(CtNewMethod.getter("getAge", enoField));
		ctClass.addMethod(CtNewMethod.setter("setAge", enoField));
		
		
		// 无参构造器
		CtConstructor constructor = new CtConstructor(null, ctClass);
		constructor.setBody("{}");
		ctClass.addConstructor(constructor);
		
		// 添加构造函数
		//ctClass.addConstructor(new CtConstructor(new CtClass[] {}, ctClass));
		
		CtConstructor ctConstructor = new CtConstructor(new CtClass[]{pool.get(String.class.getName()), CtClass.intType}, ctClass);
		ctConstructor.setBody("{\n this.username=$1; \n this.age=$2;\n}");
		ctClass.addConstructor(ctConstructor);
		
		// 添加自定义方法
		CtMethod ctMethod = new CtMethod(CtClass.voidType, "printUser", new CtClass[]{}, ctClass);
		// 为自定义方法设置修饰符
		ctMethod.setModifiers(Modifier.PUBLIC);
		// 为自定义方法设置函数体
		StringBuffer buffer2 = new StringBuffer();
		buffer2.append("{\nSystem.out.println(\"用户信息如下\");\n").append("System.out.println(\"用户名=\"+username);\n").append("System.out.println(\"年龄=\"+age);\n").append("}");
		ctMethod.setBody(buffer2.toString());
		ctClass.addMethod(ctMethod);
		
		//生成一个class
		Class<?> clazz = ctClass.toClass();
		
		Constructor cons2 = clazz.getDeclaredConstructor(String.class, Integer.TYPE);
		
		Object obj = cons2.newInstance("itheima", 20);
		
		//反射 执行方法
		obj.getClass().getMethod("printUser", new Class[]{}).invoke(obj, new Object[]{});
		
		// 把生成的class文件写入文件
		byte[] byteArr = ctClass.toBytecode();
		FileOutputStream fos = new FileOutputStream(new File("C:\\myfile\\yida-jgs\\a-my-test\\src\\main\\java\\com\\yida\\compiler\\yida"));
		fos.write(byteArr);
		fos.close();
	}
}