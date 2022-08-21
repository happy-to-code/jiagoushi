package com.yida;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MapDemo {
	public static void main(String[] args) {
		ConcurrentMap<String, Integer> map = new ConcurrentHashMap<>(10);
		map.putIfAbsent("a", 1);
		map.putIfAbsent("a", 2);
		map.putIfAbsent("b", 3);
		
		System.out.println("map = " + map);
	}
}
