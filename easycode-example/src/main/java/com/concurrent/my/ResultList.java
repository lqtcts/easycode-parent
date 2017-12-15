package com.concurrent.my;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ResultList{
	
	public static List<String> arrayList = new ArrayList<>();
	
	//锁
	public static Lock lock = new ReentrantLock();
	
	public static void addResult(List<String> list){
		lock.lock();//加锁
		for (int i = 0; i < list.size(); i++) {
			arrayList.add(list.get(0));
		}
		lock.unlock();//释放
	}

}
