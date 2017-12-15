package com.concurrent.mylearn;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierTest {

	
	public static void main(String[] args) {
		
		CyclicBarrier cyclicBarrier = new CyclicBarrier(3, new Runnable() {
			
			@Override
			public void run() {
				System.out.println("start game");
				
			}
		});
		
		new Player(cyclicBarrier, "a").start();
		new Player(cyclicBarrier, "b").start();
		new Player(cyclicBarrier, "c").start();
		
		
	}
}
class Player extends Thread{

	private CyclicBarrier cyclicBarrier;

	public Player(CyclicBarrier cyclicBarrier,String name) {
		setName(name);
		this.cyclicBarrier = cyclicBarrier;
	}
	
	public void run(){
		System.out.println("  is waiting other players...");
		try {
			cyclicBarrier.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		}
	}
	
}
