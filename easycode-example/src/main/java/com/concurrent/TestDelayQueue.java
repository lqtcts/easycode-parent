package com.concurrent;

import java.util.Random;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 在现实生活中，很多DelayQueue的例子。就拿上海的SB会来说明，很多国家地区的开馆时间不同。你很早就来到园区，然后急急忙忙地跑到一些心仪的馆区，
 * 发现有些还没开，你吃了闭门羹。
 * 仔细研究DelayQueue，你会发现它其实就是一个PriorityQueue的封装（按照delay时间排序），里面的元素都实现了Delayed接口
 * ，相关操作需要判断延时时间是否到了。 在实际应用中，有人拿它来管理跟实际相关的缓存、session等 下面我就通过
 * “上海SB会的例子来阐述DelayQueue的用法
 */
public class TestDelayQueue {
	private class Stadium implements Delayed {
		long trigger;

		public Stadium(long i) {
			trigger = System.currentTimeMillis() + i;
		}

		@Override
		public long getDelay(TimeUnit arg0) {
			long n = trigger - System.currentTimeMillis();
			return n;
		}

		@Override
		public int compareTo(Delayed arg0) {
			return (int) (this.getDelay(TimeUnit.MILLISECONDS) - arg0.getDelay(TimeUnit.MILLISECONDS));
		}

		public long getTriggerTime() {
			return trigger;
		}
	}

	public static void main(String[] args) throws Exception {
		Random random = new Random();
		DelayQueue<Stadium> queue = new DelayQueue<Stadium>();
		TestDelayQueue t = new TestDelayQueue();
		for (int i = 0; i < 5; i++) {
			queue.add(t.new Stadium(random.nextInt(30000)));
		}
		Thread.sleep(2000);
		while (true) {
			Stadium s = queue.take();// 延时时间未到就一直等待
			if (s != null) {
				System.out.println(System.currentTimeMillis() - s.getTriggerTime());// 基本上是等于0
			}
			if (queue.size() == 0)
				break;
		}
	}
}