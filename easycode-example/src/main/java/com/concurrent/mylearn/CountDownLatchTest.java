package com.concurrent.mylearn;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchTest {
	public static void main(String[] args) {
		CountDownLatch countDownLatch = new CountDownLatch(3);
		new Racer(countDownLatch, "小明").start();
		new Racer(countDownLatch, "笑话").start();
		new Racer(countDownLatch, "小何").start();
		
		for (int i = 0; i < 3; i++) {
			try {
				Thread.sleep(1000);
				countDownLatch.countDown();
				System.out.println(3-i);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
}

class Racer extends Thread{
	private CountDownLatch countDownLatch;
	
	public Racer(CountDownLatch countDownLatch,String name){
		setName(name);
		this.countDownLatch=countDownLatch;
	}
	
	public void run(){
		
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(getName()+" run");
	}
}
