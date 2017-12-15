package kafkatest;

import java.util.Date;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

/**
 * Hello world!
 *
 */
public class KafkaProducer  {
	private final Producer<String, String> producer;
	public final static String ZK_LIST = "pxene01:9092,pxene02:9093,pxene03:9094,pxene04:9095,pxene05:9096";
//	public final static String ZK_LIST = "KF01:9092,KF02:9093,KF03:9094,KF04:9095,KF05:9096";//开发
//	public final static String ZK_LIST = "192.168.3.161:2181,192.168.3.162:2181,192.168.3.163:2181,192.168.3.164:2181,192.168.3.165:2181";
//	public static final String ZK_LIST = "pxene07:9092,pxene08:9093,pxene09:9094,pxene10:9095,pxene11:9096"; //测试的地址91


	
	public KafkaProducer() {
		Properties props = new Properties();
		// 此处配置的是kafka的端口
		props.put("metadata.broker.list", ZK_LIST);
		// 配置value的序列化类
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		// 配置key的序列化类
		props.put("key.serializer.class", "kafka.serializer.StringEncoder");
		
		props.put("request.required.acks", "-1");
		producer = new Producer<String, String>(new ProducerConfig(props));
	}

	void produce(String topic,String msg ,int num) {
		int messageNo = 0;
		final int COUNT = num;

		while (messageNo < COUNT) {
			producer.send(new KeyedMessage<String, String>(topic, "str", msg));
			messageNo++;
		}
		System.out.println("--send end");
	}

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		System.out.println("--startTime:"+new Date());
		try {
			if (args==null||args.length!=3) {
				System.out.println("请输入完整参数...");
				System.out.println("args ：topic data dataSendNum");
				System.out.println("like this : newCounter \"xxx|xxx|xxx|xxx|xxx\" 10000");
			}else {
				System.out.println("--send msg to kafka start.....");
				String data = args[0];
				String inteString = args[1];
				String topic = args[2];
				new KafkaProducer().produce(topic,data,Integer.parseInt(inteString));
				
				System.out.println("--complete time:"+(System.currentTimeMillis()-start)/1000+"s");
			}
		} catch (Exception e) {
			System.out.println("error...");
			e.printStackTrace();
		}
	}
}