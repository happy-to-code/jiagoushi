package com.yida;

import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class BlockListDemo {
	@Test
	public void blockQueueTest() throws InterruptedException {
		BlockingQueue<String> queue = new ArrayBlockingQueue<>(2);
		queue.put("a");
		queue.put("b");
		// queue.put("c"); // 阻塞
		System.out.println("1---->" + System.currentTimeMillis() / 1000);
		queue.offer("c", 2, TimeUnit.SECONDS); // 阻塞 有超时时间
		System.out.println("2---->" + System.currentTimeMillis() / 1000);
		System.out.println("------------>" + queue);
	}
}
