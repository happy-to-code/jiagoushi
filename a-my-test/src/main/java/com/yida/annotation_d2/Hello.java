package com.yida.annotation_d2;

@Report(value = "hello_1",level = "debug",type = 1)
public class Hello {
	@Report(value = "name")
	private String name;
	
	private Integer age;
	
	
	public Hello(String name) {
		this.name = name;
	}
}
