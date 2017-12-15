package com.stream.storm.trident;

import storm.kafka.BrokerHosts;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;
import storm.kafka.trident.TransactionalTridentKafkaSpout;
import storm.kafka.trident.TridentKafkaConfig;
import storm.trident.TridentTopology;
import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.tuple.Fields;

import com.stream.storm.util.Const;

public class CounterTopo {

	public static void main(String[] args) throws Exception {
//		FixedBatchSpout spout = new FixedBatchSpout(new Fields("str"), 3,
//	               new Values(
//	            		    "|123|1101|192.168.333.333|111|wwww.dddddd.ccc|eww2.ee.ecc|2|2|200x200|1|3|1111|sadsad-sddd-sss|3|wwww.ddddjujj.cc|xiaomi"
//	            		   ));
		BrokerHosts brokerHosts = new ZkHosts(Const.ZK_STR);
		TridentKafkaConfig kafkaConfig = new TridentKafkaConfig(brokerHosts, Const.COUNTER_TOPIC, "xxxxxx");
		kafkaConfig.scheme = new SchemeAsMultiScheme(new StringScheme());

		TransactionalTridentKafkaSpout kafkaSpout = new TransactionalTridentKafkaSpout(kafkaConfig);
		TridentTopology topology = new TridentTopology();
		
		topology.newStream("20160405c", kafkaSpout)
			.parallelismHint(20)
			.shuffle()
			.each(new Fields("str"), new Senquece(), new Fields("mapid", "type", "category", "time", "count"))
			.parallelismHint(80)
			.groupBy(new Fields("mapid", "type", "category", "time"))
			.aggregate(new Fields("count"), new SumAgg(), new Fields("sum"))
			.parallelismHint(35)
			.each(new Fields("mapid", "type", "category", "time", "sum"), new StoreFilter())
			.parallelismHint(20);

		Config conf = new Config();
		conf.put(Config.NIMBUS_HOST, Const.NIMBUS_HOST);
		conf.setMaxSpoutPending(10000);
		conf.setMessageTimeoutSecs(120);
		conf.setNumWorkers(10);
		
		StormSubmitter.submitTopology(Const.COUNTER_TOPO, conf, topology.build());
		
//		//提交到本地
//		Config conf = new Config();
//		conf.setDebug(false);
//		LocalCluster cluster = new LocalCluster();
//		cluster.submitTopology(Const.PVUV_TOPO, conf, topology.build());		
	}
}
