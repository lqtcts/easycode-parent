package com.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * FutureTask一个可取消的异步计算，FutureTask 实现了Future的基本方法，提空 start cancel
 * 操作，可以查询计算是否已经完成， 并且可以获取计算的结果。 结果只可以在计算完成之后获取，get方法会阻塞当计算没有完成的时候，一旦计算已经完成，
 * 那么计算就不能再次启动或是取消。 一个FutureTask 可以用来包装一个 Callable
 * 或是一个runnable对象。因为FurtureTask实现了Runnable方法，所以一个
 * FutureTask可以提交(submit)给一个Excutor执行(excution).
 * 
 * @author wuchengbin
 *FutureTask其实就是新建了一个线程单独执行，使得线程有一个返回值，方便程序的编写
 */
public class TestFutureTask {

	/**
	 * 我们的算法中有一个很耗时的操作，在编程的是，我们希望将它独立成一个模块，调用的时候当做它是立刻返回的，并且可以随时取消的
	 * 
	 * @param args
	 */
	  public static void main(String[] args) {
	        ExecutorService exec = Executors.newCachedThreadPool();

	        FutureTask<String> task = new FutureTask<String>(
	                new Callable<String>() {// FutrueTask的构造参数是一个Callable接口
	                    public String call() throws Exception {
	                        return Thread.currentThread().getName();// 这里可以是一个异步操作
	                    }
	                });

	        try {
	            exec.execute(task);// FutureTask实际上也是一个线程
	            String result = task.get();// 取得异步计算的结果，如果没有返回，就会一直阻塞等待
	            System.out.printf("get:%s%n", result);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        } catch (ExecutionException e) {
	            e.printStackTrace();
	        }
	    }
}
