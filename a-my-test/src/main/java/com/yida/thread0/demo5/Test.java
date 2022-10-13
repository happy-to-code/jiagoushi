package com.yida.thread0.demo5;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：zhangyifei
 * @date ：Created in 2022/10/12 16:54
 * @description：
 * @modified By：
 * @version:
 */
public class Test {
	// 	实现一个容器，提供两个方法，add，size 写两个线程，线程1添加10个元素到容器中，
	//	线程2实现监控元素的个数，当个数到5个时，线程2给出提示并结束.
	volatile List list = new ArrayList<>();

	public void add(int i) {
		list.add(i);
	}

	public int getSize() {
		return list.size();
	}

	public static void main(String[] args) {
		Test test = new Test();
		Object lock = new Object();

		new Thread(() -> {
			synchronized (lock) {
				System.out.println("t2线程启动");
				if (test.getSize() != 5) {
					try {
						lock.wait();
						System.out.println("t2线程结束");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				lock.notify(); // 唤醒其他线程
			}

		}, "t2").start();

		new Thread(() -> {
			synchronized (lock) {
				System.out.println("t1启动");
				for (int i = 0; i < 9; i++) {
					test.add(i);
					System.out.println("add i = " + i);
					if (test.getSize() == 5) {
						lock.notify();

						try {
							lock.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}, "t1").start();
	}
}
