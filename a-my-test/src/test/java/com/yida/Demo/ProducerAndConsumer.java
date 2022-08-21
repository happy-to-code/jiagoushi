package com.yida.Demo;

import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerAndConsumer {
	public static void main(String[] args) {
		BlockingQueue blockingQueue = new ArrayBlockingQueue(10);
		// 创建两个生产者
		Producer p1 = new Producer(blockingQueue);
		Producer p2 = new Producer(blockingQueue);
		
		Consumer consumer = new Consumer(blockingQueue);
		
		new Thread(p1).start();
		new Thread(p2).start();
		new Thread(consumer).start();
	}
	
}

// 消费者
class Producer implements Runnable {
	private BlockingQueue queue;
	
	public Producer(BlockingQueue q) {
		this.queue = q;
	}
	
	@Override
	public void run() {
		for (int i = 0; i < 5; i++) {
			try {
				queue.put(makeProduct(i));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	// 生成产品的方法
	Object makeProduct(int i) {
		String s = i + ":\t" + UUID.randomUUID().toString();
		System.out.println(Thread.currentThread().getName() + " :" + s);
		return s;
	}
}

// 消费者
class Consumer implements Runnable {
	private BlockingQueue queue;
	
	public Consumer(BlockingQueue q) {
		this.queue = q;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				consumeProduct(queue.take());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	void consumeProduct(Object obj) {
		System.out.println("消费者消费产品：" + obj.toString());
	}
}