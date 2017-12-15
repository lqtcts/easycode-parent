package com.stream.flume.flume2kafka.sink;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.flume.Channel;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.Transaction;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;

import java.util.Properties;

public class KafkaMotionSink extends AbstractSink implements Configurable {
	
	private static final Log logger = LogFactory.getLog(KafkaMotionSink.class);
    
    private Producer<String, String> producer;
   
    public Status process() throws EventDeliveryException {
    	Channel channel = getChannel();
    	Transaction tx = channel.getTransaction();
    	try {
           tx.begin();
           Event e = channel.take();
           if(e == null) {
               tx.rollback();
               return Status.BACKOFF;
           }
           String content = new String(e.getBody(), "UTF-8");
           KeyedMessage<String,String> data = new KeyedMessage<String, String>("newCounter", content);
           producer.send(data);
           tx.commit();
           return Status.READY;
       } catch(Exception e) {
    	   logger.info("Flume receive data error :" + e.getMessage());
           tx.rollback();
           return Status.BACKOFF;
       } finally {
           tx.close();
       }
    }

    public void configure(Context context) {
          Properties props = new Properties();
          props.setProperty("metadata.broker.list", "pxene01:9092,pxene02:9093,pxene03:9094,pxene04:9095,pxene05:9096");
          props.setProperty("serializer.class","kafka.serializer.StringEncoder");
          ProducerConfig config = new ProducerConfig(props);
          producer = new Producer<String, String>(config);
    }

}

