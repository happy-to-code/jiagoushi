package com.yida.thread0.demo2;

class MyThread extends Thread {

	public void run() {
		synchronized (this) {
			System.out.println("2	before notify");
			notify();
			System.out.println("3	after notify");
		}
	}
}

public class WaitAndNotifyDemo {
	public static void main(String[] args) throws InterruptedException {
		MyThread myThread = new MyThread();
		synchronized (myThread) {
			try {
				myThread.start();
				// 主线程睡眠3s
				// Thread.sleep(3000);
				System.out.println("1	before wait");
				// 阻塞主线程
				myThread.wait();
				System.out.println("4	after wait");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
  