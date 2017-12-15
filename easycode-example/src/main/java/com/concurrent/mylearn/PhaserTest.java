package com.concurrent.mylearn;

import java.util.concurrent.Phaser;

public class PhaserTest {

	
	public static void main(String[] args) {
		Phaser phaser = new Phaser(1);
		
		
		System.out.println("Starting...");
		
		new Worker(phaser, "FWY").start();
		new Worker(phaser, "CC").start();
		new Worker(phaser, "SC").start();
		
		
		for (int i = 0; i <=3; i++) {
			phaser.arriveAndAwaitAdvance();
			System.out.println("order +"+i+" finished!");
		}
		
		phaser.arriveAndDeregister();
		System.out.println("all");
	}
}


class Worker extends Thread{
	private Phaser phaser;

	public Worker(Phaser phaser,String name) {
		setName(name);
		this.phaser = phaser;
		phaser.register();
	}
	
	public void run(){
		for (int i = 0; i <= 3; i++) {
			System.out.println("currnet order  .... "+i+""+getName());
			
			if (i==3) {
				phaser.arriveAndDeregister();
			}else {
				phaser.arriveAndAwaitAdvance();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
}