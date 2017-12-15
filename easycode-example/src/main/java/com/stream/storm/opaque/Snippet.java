package com.stream.storm.opaque;


import storm.kafka.ZkHosts;
import storm.kafka.trident.OpaqueTridentKafkaSpout;
import storm.kafka.trident.TridentKafkaConfig;
import storm.trident.Stream;
import storm.trident.TridentState;
import storm.trident.TridentTopology;
import storm.trident.testing.FixedBatchSpout;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class Snippet {
	public static void main(String[] args) {
		buildTopology("","");
		System.out.println("Snippet.main()");
	}

	public static void buildTopology(String zkHostsStr, String topic) {
		FixedBatchSpout spouttest = new FixedBatchSpout(new Fields("str"), 3, 
				new Values("111|2|1|1|1|1|1|1|1|1|1|1|1|1"), 
				new Values("111|2|1|1|1|1|1|1|1|1|1|1|1|1"), 
				new Values( "111|2|1|1|1|1|1|1|1|1|1|1|1|1"),
				new Values("111|2|1|1|1|1|1|1|1|1|1|1|1|1"));

		ZkHosts zkHosts = new ZkHosts(zkHostsStr);
		TridentTopology indexerTopology = new TridentTopology();
		TridentKafkaConfig tridentKafkaConfig = new TridentKafkaConfig(zkHosts, topic);
		OpaqueTridentKafkaSpout spout = new OpaqueTridentKafkaSpout(tridentKafkaConfig);

		
		
		TridentTopology topology = new TridentTopology();
		TridentState locations = topology.newStaticState(new LocationDBFactory());

		  topology.newStream("myspout", spouttest)
				 .each(new Fields("str"), new Senquece(), new Fields("mapid", "type", "category", "time", "count"));
//		 .stateQuery(locations, new Fields("mapid"), new QueryLocation(), new  Fields("mapid")) ;

		 
//		stateQuery
//		.partitionPersist(new LocationDBFactory(), new Fields("mapid"), new LocationUpdater());

		
	 
		Config conf = new Config();
		conf.setDebug(false);
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("test", conf, topology.build());
	}
}
