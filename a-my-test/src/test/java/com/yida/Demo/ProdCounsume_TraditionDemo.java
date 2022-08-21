package com.yida.Demo;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProdCounsume_TraditionDemo {
	
	public static void main(String[] args) {
		
		ShareData shareData = new ShareData();
		
		new Thread(() -> {
			for (int i = 0; i <= 5; i++) {
				shareData.increment();
			}
		}, "T1").start();
		
		new Thread(() -> {
			for (int i = 0; i <= 5; i++) {
				shareData.decrement();
			}
		}, "T2").start();
	}
}

//线程操作资源类
class ShareData {
	private int num = 0;
	private Lock lock = new ReentrantLock();
	private Condition condition = lock.newCondition();
	
	public void increment() {
		lock.lock();
		try {
			while (num != 0) {
				//等待，不能生产
				condition.await();
			}
			//干活
			num++;
			System.out.println(Thread.currentThread().getName() + "\t" + num);
			
			//唤醒
			condition.signal();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	public void decrement() {
		lock.lock();
		try {
			while (num == 0) {
				//等待，不能生产
				condition.await();
			}
			//干活
			num--;
			System.out.println(Thread.currentThread().getName() + "\t" + num);
			//唤醒
			condition.signal();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
}