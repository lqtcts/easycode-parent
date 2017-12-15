package com.stream.storm.bolt;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class SplitBolt extends BaseBasicBolt {

	private static final Log logger = LogFactory.getLog(SplitBolt.class);

	private static final long serialVersionUID = 474023451052581446L;

	private int a;
	
	private static ThreadLocal<SimpleDateFormat> formatCache = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:00:00");
		};
	};

	public void execute(Tuple input, BasicOutputCollector collector) {
//		logger.info("split:"+input);
		DateFormat sdf = formatCache.get();
		String str = input.getString(0);
		String[] motionInfos = str.split("\\|");
		if (motionInfos.length == 7) {
			// 发送mapid, type, category, time, 1到下一步
			collector.emit(new Values(motionInfos[4]+","+motionInfos[5]+","+ motionInfos[6]+","+ sdf.format(new Date(Long.parseLong(motionInfos[0])))));
		} else {
			logger.error("数据错误：" + str);
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("sources"));
	}

}
