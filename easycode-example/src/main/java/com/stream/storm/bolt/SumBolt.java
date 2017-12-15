package com.stream.storm.bolt;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

/**
 * ------------------------------------------------------------------------- 
* 版权所有：上海蓬景数字营销策划有限公司 
* 作者：@author wuchengbin 
* 联系方式：wuchengbin@pxene.com 
* 创建时间： 2016年6月29日 下午3:51:18
* 版本号：v1.0 
* 本类主要用途描述： 多线程局部汇总，counter数据
-------------------------------------------------------------------------
 */
public class SumBolt   implements IRichBolt {
	private static final long serialVersionUID = -2591232382374837732L;

	OutputCollector collector;

	Map<String, Integer> counts = new HashMap<String, Integer>();
	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		//获取参数作为key 统计数据
		String mapid = input.getStringByField("mapid");
		String type = input.getStringByField("type");
		String category = input.getStringByField("category");
		String time = input.getStringByField("time");
		String key = mapid + "_" + type + "_" + category + "_" + time;
		Integer count = 0;
		try {
			count = counts.get(key);
			if (count == null) {
				count = 0;
			}
			count++;
			counts.put(key, count);
			System.out.println("SumBolt.execute()"+count);
			this.collector.emit(new Values(key, count+""));
			this.collector.ack(input);
		} catch (Exception e) {
			e.printStackTrace();
			this.collector.fail(input);
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		 declarer.declare(new Fields("mapid_key", "count"));  
	}

	@Override
	public void cleanup() {
		Set<String> keySet = this.counts.keySet();
		for (String string : keySet) {
			System.out.println("---------"+string+"------" +this.counts.get(string));
			this.collector.emit(new Values(string, this.counts.get(string)));
		}
		this.counts.clear();
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}
	
	

}
