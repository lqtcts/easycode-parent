package kafkatest;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestThread {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		long start = System.currentTimeMillis();
		System.out.println("--startTime:" + new Date());
		try {
			if (args == null || args.length == 0) {
				System.out.println("请输入完整参数...");
				System.out.println("args ：\"topic(要给kafka哪个频道发送),data(要发送的数据),dataSendNum(要发送多少数据),threadNum(多少个线程发送完这些数据)\" ... ...");
				System.out.println("like this : \"newCounter,xxx|xxx|xxx|xxx|xxx,10000,10\" ... ...");
			} else {
				System.out.println("---------------------send msg to kafka start.....");

				execWrite(args);
				System.out.println("---------------------complete time:" + (System.currentTimeMillis() - start) / 1000 + "s");
			}
		} catch (Exception e) {
			System.out.println("error...");
			e.printStackTrace();
		}
	}

	/**
	 * 使用线程池的方式是复用线程的（推荐） 而不使用线程池的方式是每次都要创建线程
	 * Executors.newCachedThreadPool()，该方法返回的线程池是没有线程上限的，可能会导致过多的内存占用
	 * 建议使用Executors.newFixedThreadPool(n)
	 * 
	 * 有兴趣还可以看下定时线程池：SecheduledThreadPoolExecutor
	 * 
	 * @param args
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static void execWrite(String[] args) throws InterruptedException, ExecutionException {
		System.out.println("---------------------------------------------------------------------------------------------------------");
		int nThreads = getNThreads(args);
		/**
		 * Executors是ThreadPoolExecutor的工厂构造方法
		 */
		ExecutorService executor = Executors.newFixedThreadPool(nThreads);
		// submit有返回值，而execute没有返回值，有返回值方便Exception的处理
		
		submitThreads(args, executor);
		
//		Future res = executor.submit(new Runner(null, null, null));
		// executor.execute(new ConsumerThread());
		/**
		 * shutdown调用后，不可以再submit新的task，已经submit的将继续执行
		 * shutdownNow试图停止当前正执行的task，并返回尚未执行的task的list
		 */
		executor.shutdown();
		// 配合shutdown使用，shutdown之后等待所有的已提交线程运行完，或者到超时。继续执行后续代码
		System.out.println("所有并发已发出... 等待执行结果");
		executor.awaitTermination(nThreads, TimeUnit.DAYS);

		
		// 打印执行结果，出错的话会抛出异常，如果是调用execute执行线程那异常会直接抛出，不好控制，submit提交线程，调用res.get()时才会抛出异常，方便控制异常
		System.out.println("---------------------------------------------------------------------------------------------------------");
	}

	public static int getNThreads(String[] args) {
		int threadN = 0;
		for (int i = 0; i < args.length; i++) {
			String string = args[i];
			String[] split = string.split(",");
			if (split.length == 4) {
				threadN += Integer.parseInt(split[3]);
			}
		}
		return threadN;
	}

	public static void submitThreads(String[] args,ExecutorService executor) {
		for (int i = 0; i < args.length; i++) {
			String string = args[i];
			String[] split = string.split(",");
			if (split.length == 4) {
				System.out.println("==开始并发："+string);
				
				String topic = split[0];
				String data = split[1];
				int dataNum =  Integer.parseInt(split[2]);
				int threadNum =  Integer.parseInt(split[3]);
				int dataOneThread = dataNum/threadNum;
				int endDataNum = dataNum-(dataOneThread*(threadNum-1));
				for (int j = 1; j <= threadNum; j++) {
					
					if (j==threadNum) {
						System.out.println("----"+j+"."+topic+","+data+","+endDataNum);
						executor.submit(new Runner(topic, data, endDataNum));
					}else {
						System.out.println("----"+j+"."+topic+","+data+","+dataOneThread);
						executor.submit(new Runner(topic, data, dataOneThread));
					}
					
				}
				
			}
		}
		
		
	}
	
}