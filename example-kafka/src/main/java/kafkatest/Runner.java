package kafkatest;

/**
 * 
 *
 */
public class Runner implements Runnable{
	
	String topic;
	String data;
	int inte;
	 
 

	public Runner(String topic, String data, int inte) {
		this.topic = topic;
		this.data = data;
		this.inte = inte;
	}

	public void run() {
		try {
			
			new KafkaProducer().produce(topic,data,inte);
		} catch (Exception e) {
			System.out.println("执行失败");
			e.printStackTrace();
		}
	}
 

}
