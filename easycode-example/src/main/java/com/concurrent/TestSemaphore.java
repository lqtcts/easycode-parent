package com.concurrent;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 一个计数信号量。从概念上讲，信号量维护了一个许可集。如有必要，
 * 在许可可用前会阻塞每一个 acquire()，然后再获取该许可。
 * 每个 release() 添加一个许可，从而可能释放一个正在阻塞的获取者。
 * 但是，不使用实际的许可对象，Semaphore 只对可用许可的号码进行计数，并采取相应的行动。 
 * Semaphore 通常用于限制可以访问某些资源（物理或逻辑的）的线程数目
 * @author wuchengbin
 *
 */
public class TestSemaphore {
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		TestSemaphore t = new TestSemaphore();
		final BoundedHashSet<String> set = t.getSet();

		for (int i = 0; i < 10; i++) {// 三个线程同时操作add
			exec.execute(new Runnable() {
				public void run() {
					try {
						set.add(Thread.currentThread().getName());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			});
		}

		for (int j = 0; j < 10; j++) {// 三个线程同时操作remove
			exec.execute(new Runnable() {
				public void run() {
					set.remove(Thread.currentThread().getName());
				}
			});
		}
		exec.shutdown();
	}

	public BoundedHashSet<String> getSet() {
		return new BoundedHashSet<String>(2);// 定义一个边界约束为2的线程
	}

	class BoundedHashSet<T> {
		private final Set<T> set;
		private final Semaphore semaphore;

		public BoundedHashSet(int bound) {
			
			/**
			 * Collection类中提供了多个synchronizedXxx方法，该方法返回指定集合对象对应的同步对象，
			 * 从而解决多线程并发访问集合时线程的安全问题。java中常用的HashSet、ArrayList、HashMap都是线程不安全的，
			 * 如果多条线程访问他们，而且多于一条的线程试图修改它们，则可能出错。以下方法直接将新建的集合传给了Collections的synchronizedXxx方法，
			 * 这样就直接获取它们的线程安全实现版本。
			 */
			this.set = Collections.synchronizedSet(new HashSet<T>());// ①
			this.semaphore = new Semaphore(bound, true);
		}

		public void add(T o) throws InterruptedException {
			semaphore.acquire();// 信号量控制可访问的线程数目
			set.add(o);
			System.out.printf("add:%s%n", o);
		}

		public void remove(T o) {
			if (set.remove(o))
				semaphore.release();// 释放掉信号量
			System.out.printf("remove:%s%n", o);
		}
	}
}