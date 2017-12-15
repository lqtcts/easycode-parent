package com.stream.kafka.kafka2hdfs;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

public class StreamConsumer implements Runnable {

    private HDFSWriter hdfsWriter;
    
    private KafkaStream<byte[], byte[]> stream;

    public StreamConsumer(HDFSWriter hdfsWriter, KafkaStream<byte[], byte[]> stream) {
        this.hdfsWriter = hdfsWriter;
        this.stream = stream;
    }

    public void run() {
        ConsumerIterator<byte[], byte[]> iterator = stream.iterator();
        while (iterator.hasNext()) {
		    hdfsWriter.write(new String(iterator.next().message()));
		}
    }
}
