package com.yida.temp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Demo {
	public static void main(String[] args) {
		List<String> list = new ArrayList<>(10);
		try {
			list.add("a");
			list.add("b");
			list.add("c");
			
			list.add(0, "aa");
			list.remove("c");
		}finally {
			System.out.println("-------------");
		}
		System.out.println("list = " + list);
	}
	Map map = new HashMap<>();
	
	ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10,20,20, TimeUnit.SECONDS,new LinkedBlockingDeque<>(10));
}
