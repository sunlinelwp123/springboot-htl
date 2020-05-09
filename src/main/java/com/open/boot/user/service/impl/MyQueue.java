package com.open.boot.user.service.impl;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class MyQueue {
	
	private final LinkedList<Object> list = new LinkedList<Object>();
	private AtomicInteger count = new AtomicInteger(0);
	private final int minSize = 0;
	private final int maxSize;

	public MyQueue(int size) {
		this.maxSize = size;
	}
	
	 public MyQueue(){
	        this(10);
	 }
	
	private final Object lock = new Object(); // 返回长度

	public int size() {
		return count.get();
	}

	public void pull(Object obj) {
		synchronized (lock) {
			while (size() == maxSize) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			list.add(obj);
			count.incrementAndGet();
			System.out.println("新加入的元素为：" + obj);
			lock.notify();
		}

	}

	public Object pop() {
		synchronized (lock) {
			while (minSize == size()) {
				try {
					lock.wait();
				} catch (InterruptedException e) { 
					e.printStackTrace();
				}
			}
			Object obj = list.removeLast();
			count.decrementAndGet();
			lock.notify();
			return obj;
		}
	}
}
