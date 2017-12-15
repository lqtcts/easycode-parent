package com.stream.storm.opaque;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import storm.kafka.BrokerHosts;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;
import storm.kafka.trident.OpaqueTridentKafkaSpout;
import storm.kafka.trident.TridentKafkaConfig;
import storm.trident.TridentTopology;
import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.operation.builtin.Count;
import storm.trident.testing.MemoryMapState;
import storm.trident.tuple.TridentTuple;
import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

/*
 * 本类完成以下内容
 */
public class SyTopology {

    public static final Logger LOG = LoggerFactory.getLogger(SyTopology.class);

    private final BrokerHosts brokerHosts;

    public SyTopology(String kafkaZookeeper) {
        brokerHosts = new ZkHosts(kafkaZookeeper);
    }

    public StormTopology buildTopology() {
        TridentKafkaConfig kafkaConfig = new TridentKafkaConfig(brokerHosts, "ma30", "storm");
        kafkaConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
        // TransactionalTridentKafkaSpout kafkaSpout = new
        // TransactionalTridentKafkaSpout(kafkaConfig);
        OpaqueTridentKafkaSpout kafkaSpout = new OpaqueTridentKafkaSpout(kafkaConfig);
        TridentTopology topology = new TridentTopology();

        // TridentState wordCounts =
        topology.newStream("kafka4", kafkaSpout).
        each(new Fields("str"), new Split(),
                new Fields("word")).groupBy(new Fields("word"))
                .persistentAggregate(new MemoryMapState.Factory(), new Count(),
                        new Fields("count")).parallelismHint(16);
        // .persistentAggregate(new HazelCastStateFactory(), new Count(),
        // new Fields("aggregates_words")).parallelismHint(2);


        return topology.build();
    }

    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
        String kafkaZk = args[0];
        SyTopology topology = new SyTopology(kafkaZk);
        Config config = new Config();
        config.put(Config.TOPOLOGY_TRIDENT_BATCH_EMIT_INTERVAL_MILLIS, 2000);

        String name = args[1];
        String dockerIp = args[2];
        config.setNumWorkers(9);
        config.setMaxTaskParallelism(5);
        config.put(Config.NIMBUS_HOST, dockerIp);
        config.put(Config.NIMBUS_THRIFT_PORT, 6627);
        config.put(Config.STORM_ZOOKEEPER_PORT, 2181);
        config.put(Config.STORM_ZOOKEEPER_SERVERS, Arrays.asList(dockerIp));
        StormSubmitter.submitTopology(name, config, topology.buildTopology());

    }

    static class Split extends BaseFunction {
        public void execute(TridentTuple tuple, TridentCollector collector) {
            String sentence = tuple.getString(0);
            for (String word : sentence.split(",")) {
                try {
                    FileWriter fw = new FileWriter(new File("/home/data/test/ma30/ma30.txt"),true);
                    fw.write(word);
                    fw.flush();
                    fw.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                collector.emit(new Values(word));
                
            }
        }
    }
}