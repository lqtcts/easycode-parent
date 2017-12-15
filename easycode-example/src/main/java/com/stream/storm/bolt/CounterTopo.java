package com.stream.storm.bolt;

import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;
import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

import com.stream.storm.util.Const;

public class CounterTopo {

	public static void main(String[] args) throws Exception {
		//zk地址
		//spout设置

		//topo设置
		TopologyBuilder builder = new TopologyBuilder();
		BrokerHosts brokerHosts = new ZkHosts(Const.ZK_STR);
		SpoutConfig spoutConf = new SpoutConfig(brokerHosts, Const.COUNTER_TOPIC, "/transactional/201607251c", "20160726c");
		spoutConf.scheme = new SchemeAsMultiScheme(new StringScheme());
		KafkaSpout spout  = new KafkaSpout(spoutConf);
		  
		//设置spout用于接收数据
		builder.setSpout("kafka-reader",spout, 20);
//		builder.setSpout("kafka-reader",new SimpleSpout(), 5);//本地测试
//		builder.setSpout("kafka-reader", new KafkaSpout(spoutConf), 5);// 设置喷发节点并分配并发数，该并发数将会控制该对象在集群中的线程数。

		//对数据进行切割
		builder.setBolt("counter-split", new SplitBolt(),80).shuffleGrouping("kafka-reader");
		
		//对数据进行聚合
		builder.setBolt("counter-sum", new BatchingBolt(10000,10000),30).fieldsGrouping("counter-split",new Fields("sources"));
		
		//对数据进行聚合
//		builder.setBolt("counter-sum", new SumBolt(),10).fieldsGrouping("counter-split", new Fields("mapid","type","category","time"));
		
		//对数据进行业务处理
		builder.setBolt("counter-services", new ServicesBolt(),20).shuffleGrouping("counter-sum");
		
//		builder.setBolt("counter-services",   new BatchBoltExecutor(new ServicesBatchBolt()),1).shuffleGrouping("counter-split");
		
		
		
		//设置
		Config conf = new Config();
		conf.put(Config.NIMBUS_HOST, Const.NIMBUS_HOST);
		conf.setMaxSpoutPending(10000);
		conf.setMessageTimeoutSecs(120);
		conf.setNumWorkers(10);
		StormSubmitter.submitTopology(Const.COUNTER_TOPO, conf, builder.createTopology());
		
//		  Config conf = new Config();  
//		  conf.setDebug(false);
//		  conf.setMaxTaskParallelism(1);
//		  LocalCluster cluster = new LocalCluster();  
//	     cluster.submitTopology("Getting-Started-Toplogie", conf,  builder.createTopology());
//	     Thread.sleep(20000);  
//	      cluster.shutdown();
	}
}
