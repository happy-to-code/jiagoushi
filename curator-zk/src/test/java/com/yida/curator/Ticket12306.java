package com.yida.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.TimeUnit;

public class Ticket12306 implements Runnable {
	private int tickets = 10;//数据库的票数
	private InterProcessMutex lock;
	
	
	public Ticket12306() {
		//重试策略
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 10);
		//2.第二种方式
		//CuratorFrameworkFactory.builder();
		CuratorFramework client = CuratorFrameworkFactory.builder()
				.connectString("112.124.52.75:2181").sessionTimeoutMs(60 * 1000)
				.connectionTimeoutMs(15 * 1000).retryPolicy(retryPolicy).build();
		
		//开启连接
		client.start();
		
		lock = new InterProcessMutex(client, "/lock");
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				//获取锁
				lock.acquire(3, TimeUnit.SECONDS);
				if (tickets > 0) {
					System.out.println(Thread.currentThread() + ":" + tickets);
					Thread.sleep(100);
					tickets--;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				//释放锁
				try {
					lock.release();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
