package com.concurrent.mylearn;

import java.util.concurrent.Semaphore;

public class SemaphoreTest {

	public static void main(String[] args) {
		Semaphore semaphore = new Semaphore(2);
		Person p1 = new Person(semaphore, "a");
		p1.start();
		Person p2 = new Person(semaphore, "b");
		p2.start();
		Person p3 = new Person(semaphore, "c");
		p3.start();
	}
	
}

class Person extends Thread{
	
	private Semaphore semaphore;
	
	public Person(Semaphore semaphore,String name){
		setName(name);
		this.semaphore=semaphore;
	}
	
	public void run(){
		System.out.println(getName()+"is watting...");
		
		try {
			semaphore.acquire();//获得许可证
			
			System.out.println(getName()+"is servering");
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(getName()+"is done");
		semaphore.release();//释放许可证
		
	}
}