package com.concurrent.forkjoin;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

public class Test {

	public static void main(String[] args) {
		List<String> modelList = modelList();
		long start = new Date().getTime();
		List<String> reslut; 
//		reslut= doForkJoin(modelList);//

//		reslut = doDistinct(modelList);//


		reslut = doDistinct2(modelList);//


		for (String string : reslut) {
			System.out.print(string+"-");
		}
		System.out.println();
		long end = new Date().getTime();
		System.out.println(end-start);
		
	}
	
	
	public static List<String> modelList() {
		List<String> data = new ArrayList<>();
		for (int i = 0; i < 100000000; i++) {
			if (i==10000) {
				data.add("333");
				data.add("333");
				data.add("333");
				data.add("333");
				data.add("333");
			}else{
				data.add("13131");
			}
		}
		return data;
	}
	
	public static List<String>  doForkJoin(List<String> modelList){
		//管理ForkJoinTask的线程池


		ForkJoinPool forkJoinPool = new ForkJoinPool();
        List<String> result;
		try {
			result = forkJoinPool.submit(new DistinctList( modelList)).get();
			return result;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<String> doDistinct(List<String> modelList){
		HashSet h = new HashSet(modelList);
		modelList.clear();
		modelList.addAll(h);
		return modelList;
	}
	public static List<String> doDistinct2(List<String> modelList){
		List<String> result =new ArrayList<>();
		
		for (String string : modelList) {
			if (!result.contains(string)) {
				result.add(string);
			}
		}
		return result;
	}
	
}