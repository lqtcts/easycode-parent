package com.stream.storm.trident;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;
import backtype.storm.tuple.Values;

public class Senquece extends BaseFunction {
	
	private static final Log logger = LogFactory.getLog(Senquece.class);

	private static final long serialVersionUID = 474023451052581446L;
	
	private static ThreadLocal<SimpleDateFormat> formatCache  = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:00:00");
		};
	};

	@Override
	public void execute(TridentTuple input, TridentCollector collector) {
		DateFormat sdf = formatCache.get();
		String str = input.getString(0);
		String[] motionInfos = str.split("\\|");
		if (motionInfos.length == 7) {
			//发送mapid, type, category, time, 1到下一步
			collector.emit(new Values(motionInfos[4], motionInfos[5], motionInfos[6],
					sdf.format(new Date(Long.parseLong(motionInfos[0]))), 1));
		} else {
			logger.error("数据错误：" + str);
		}
	}
	
}