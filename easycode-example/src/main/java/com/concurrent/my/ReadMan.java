package com.concurrent.my;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.utils.DateUtil;
import com.yammer.metrics.core.HealthCheck.Result;

public class ReadMan {

	public static void main(String[] args) throws Exception {
		// 线程个数
		final int COUNT = 10;
		//正则表达式
		final String reg = "GlobalPartitionInformation";
		//文件路径
//		String filePath = "C:\\Users\\Administrator\\Desktop\\a.txt";
        final String filePath = "C:\\Users\\wuchengbin\\Desktop\\新建文件夹\\worker-6709.log";  

		//文件读取io
		BufferedReader bufferedReader;
		try {
			System.out.println("read start...");
			long start=new Date().getTime();
			//读取文件
			bufferedReader = new BufferedReader(new FileReader(new File(filePath)));
			//同步计数器
			CountDownLatch doneSignal = new CountDownLatch(COUNT);
			//执行器
			ExecutorService executorService = Executors.newFixedThreadPool(COUNT);
			for (int i = 0; i < COUNT; i++) {
				ReadLogWorker readLogWorker = new ReadLogWorker(bufferedReader, reg, doneSignal);
				executorService.execute(readLogWorker);
			}
			//等待同步计时器完成
			doneSignal.await();  
			//后续操作
			List<String> arrayList = ResultList.arrayList;
			System.out.println(arrayList.size());
			bufferedReader.close();
			System.out.println( "read success....");
			long end=new Date().getTime();
			long time =end-start;
			System.out.println("time:"+time);
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
}
