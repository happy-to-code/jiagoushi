package com.yida.demo.deque;

import java.util.ArrayDeque;
import java.util.Deque;

public class Demo {
	public static void main(String[] args) {
		Deque<Integer> deque = new ArrayDeque<>(10);
		// Deque<Integer> deque = new LinkedList<>();
		
		deque.add(1);
		deque.add(2);
		deque.addFirst(-1);
		System.out.println("deque = " + deque);
		deque.add(3);
		deque.addLast(100);
		System.out.println("deque = " + deque);
	}
}
