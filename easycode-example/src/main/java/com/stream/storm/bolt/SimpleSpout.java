package com.stream.storm.bolt;


import java.util.Map;
import java.util.Random;

import storm.trident.spout.IBatchSpout;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IBasicBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.topology.base.BaseTransactionalSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

/**
 * Spout起到和外界沟通的作用，他可以从一个数据库中按照某种规则取数据，也可以从分布式队列中取任务
 * 
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class SimpleSpout   extends BaseRichSpout{
    //用来发射数据的工具类
    private SpoutOutputCollector collector;
    private static String[] info = new String[]{
    		"1469514686000|111|11|96|37c3c321-2638-410a-84de-886eb0d0f3c5|m|3",
//           "11|22|33|44|55|66|77",
           "11|22|33|44|555|66|77"};
    
    Random random=new Random();
    
    /**
     * 初始化collector
     */
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
    }
    
    /**
     * 在SpoutTracker类中被调用，每调用一次就可以向storm集群中发射一条数据（一个tuple元组），该方法会被不停的调用
     */
    @Override
    public void nextTuple() {
        try {
        	int b=0;
        	System.out.println("--------------------------------------------------------------------------");
        	for (int j = 0; j < 10; j++) {
        		for (int i = 0; i < info.length; i++) {
        			b++;
        			String msg = info[i];
        			collector.emit(new Values(msg));
        			System.out.println("abf");
        		}
			}
        	System.out.println(b+"--------------------------------------------------------------------------");
        	Thread.sleep(100000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 定义字段id，该id在简单模式下没有用处，但在按照字段分组的模式下有很大的用处。
     * 该declarer变量有很大作用，我们还可以调用declarer.declareStream();来定义stramId，该id可以用来定义更加复杂的流拓扑结构
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("str")); //collector.emit(new Values(msg));参数要对应
    }

}