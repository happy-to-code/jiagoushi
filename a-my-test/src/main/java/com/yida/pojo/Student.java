package com.yida.pojo;

public class Student {
	private String name;
	public Integer age;
	protected String address;
	Integer gender;
	
	public void show1(String s) {
		System.out.println("调用了：公有的，String参数的show1(): s = " + s);
	}
	
	protected void show2() {
		System.out.println("调用了：受保护的，无参的show2()");
	}
	
	void show3() {
		System.out.println("调用了：默认的，无参的show3()");
	}
	
	private String show4(int age) {
		System.out.println("调用了，私有的，并且有返回值的，int参数的show4(): age = " + age);
		return "abcd";
	}
	
	public void sayHello(String name) {
		System.out.println(name + "--hello、world--");
	}
	
	public void sayHello() {
		System.out.println("name = " + this.name);
	}
	
	public Student() {
	}
	
	// 私有构造方法
	private Student(Integer age) {
		System.out.println("私有的构造方法   年龄：" + age);
	}
	
	public Student(String name) {
		this.name = name;
	}
	
	public Student(String name, Integer age, String address, Integer gender) {
		this.name = name;
		this.age = age;
		this.address = address;
		this.gender = gender;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getAge() {
		return age;
	}
	
	public void setAge(Integer age) {
		this.age = age;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public Integer getGender() {
		return gender;
	}
	
	public void setGender(Integer gender) {
		this.gender = gender;
	}
	
	@Override
	public String toString() {
		return "Student{" + "name='" + name + '\'' + ", age=" + age + ", address='" + address + '\'' + ", gender=" + gender + '}';
	}
}
