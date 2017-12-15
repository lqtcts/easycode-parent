package com.timer;

public class Test {
	public static void main(String[] args) throws Exception {
		System.out.println("【系统启动】开始(每1秒输出一次)...");  
		String job=new TestTimer().getClass().getName() ;
		String job_name="job-1";
		QuartzManager.addJob(job_name, job, "0/1 * * * * ?");  
		//QuartzManager.addJob(job_name, job, "0 0/3 8-20 ? ? *");  
		  
		Thread.sleep(5000);  
		System.out.println("【修改时间】开始(每2秒输出一次)...");  
		QuartzManager.modifyJobTime(job_name, "10/2 * * * * ?");  
		Thread.sleep(6000);  
		System.out.println("【移除定时】开始...");  
		QuartzManager.removeJob(job_name);  
		System.out.println("【移除定时】成功");  
		  
//		System.out.println("/n【再次添加定时任务】开始(每10秒输出一次)...");  
//		QuartzManager.addJob(job_name, job, "*/10 * * * * ?");  
//		Thread.sleep(60000);  
//		System.out.println("【移除定时】开始...");  
//		QuartzManager.removeJob(job_name);  
//		System.out.println("【移除定时】成功");  	}
		System.out.println("/n【再次添加定时任务】开始(每10秒输出一次)...");  
		QuartzManager.addJob(job_name, job, "06 00 * ? * *");  
		Thread.sleep(60000);  
		System.out.println("【移除定时】开始...");  
		QuartzManager.removeJob(job_name);  
		System.out.println("【移除定时】成功");  	}
}
