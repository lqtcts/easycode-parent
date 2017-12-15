package com.concurrent;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch类是一个同步计数器,构造时传入int参数,该参数就是计数器的初始值，每调用一次countDown()方法，计数器减1,计数器大于0 时，await()方法会阻塞程序继续执行
 * @author wuchengbin
 *
 */
public class TestCountDownLatch {
	private static final int N = 10;

	public static void main(String[] args) throws InterruptedException {
		CountDownLatch doneSignal = new CountDownLatch(N);
		CountDownLatch startSignal = new CountDownLatch(1);// 开始执行信号

		for (int i = 1; i <= N; i++) {
			new Thread(new Worker(i, doneSignal, startSignal)).start();// 线程启动了
		}
		System.out.println("begin------------");
		startSignal.countDown();// 开始执行啦
		doneSignal.await();// 等待所有的线程执行完毕
		System.out.println("Ok");

	}

	static class Worker implements Runnable {
		private final CountDownLatch doneSignal;
		private final CountDownLatch startSignal;
		private int beginIndex;

		Worker(int beginIndex, CountDownLatch doneSignal, CountDownLatch startSignal) {
			this.startSignal = startSignal;
			this.beginIndex = beginIndex;
			this.doneSignal = doneSignal;
		}

		public void run() {
			try {
				startSignal.await(); // 等待开始执行信号的发布
				beginIndex = (beginIndex - 1) * 10 + 1;
				for (int i = beginIndex; i < beginIndex + 10; i++) {
					System.out.println(i);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				doneSignal.countDown();
			}
		}
	}
}