package com.stream.storm.bolt;

import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class BatchingBolt implements IRichBolt {
	private static final long serialVersionUID = 1L;
	private static final Log logger = LogFactory.getLog(BatchingBolt.class);
	private OutputCollector collector;
//	private Queue<Tuple> tupleQueue = new ConcurrentLinkedQueue<Tuple>();
//	private Queue<String> string = new ConcurrentLinkedQueue<String>();
	private Map<String, Integer> map = new ConcurrentHashMap<>();
	private Map<String, Integer> tempMap = new ConcurrentHashMap<>();

	private int count;
	private int sumCount;
	private long lastTime;
	
	
	private int timerInterval;

	/**
	 * @param dataPreCount 暂存多少条
	 * @param timerInterval 多长时间持久化数据   毫秒
	 */
	public BatchingBolt(int dataPreCount,int timerInterval) {
		count = dataPreCount; // 批量处理的Tuple记录条数
		lastTime = System.currentTimeMillis(); // 上次批量处理的时间戳
		this.timerInterval=timerInterval;
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				execute(null);
			}
		}, 0, timerInterval);

	}

	@Override
	public void execute(Tuple tuple) {
		if (tuple != null) {
			String string2 = tuple.getString(0);
			sumCount++;
			if (map.containsKey(string2)) {
				map.put(string2, map.get(string2) + 1);
			} else {
				map.put(string2, 1);
			}
			collector.ack(tuple); // 进行ack
		}
		long currentTime = System.currentTimeMillis();

		// 每count条tuple批量提交一次，或者每个1秒钟提交一次
		if (sumCount >= count || currentTime >= lastTime + timerInterval) {
			sumCount = 0;
			tempMap = new ConcurrentHashMap<>(map);
			map.clear();
			//遍历map
			Set<String> keySet = tempMap.keySet();
			for (String string : keySet) {
				collector.emit(new Values(string+","+tempMap.get(string)));
//				System.out.println("BatchingBolt.execute()  " + string + ":" + tempMap.get(string));
			}
//			System.out.println("batch insert data into database, total records: " + count);
			lastTime = currentTime;
		}

	}

	@Override
	public void cleanup() {
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("counterdata"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}
}