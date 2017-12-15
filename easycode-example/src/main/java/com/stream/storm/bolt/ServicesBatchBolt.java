package com.stream.storm.bolt;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.esotericsoftware.kryo.io.Input;

import backtype.storm.coordination.BatchOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBatchBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class ServicesBatchBolt extends BaseBatchBolt {

	BatchOutputCollector _collector;
	Object _id;
	Set<String> _followers = new HashSet<String>();

	@Override
	public void prepare(Map conf, TopologyContext context, BatchOutputCollector collector, Object id) {
		_collector = collector;
		_id = id;
	}

	@Override
	public void execute(Tuple tuple) {
		System.out.println("ServicesBatchBolt.execute()"+tuple);
		_followers.add(tuple.getString(1));
	}

	@Override
	public void finishBatch() {
		System.out.println("ServicesBatchBolt.finishBatch()");
		_collector.emit(new Values(_id, _followers.size()));
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		System.out.println("ServicesBatchBolt.declareOutputFields()");
		declarer.declare(new Fields("id", "partial-count"));
	}

}
