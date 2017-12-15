package com.stream.storm.trident;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import storm.trident.operation.Filter;
import storm.trident.operation.TridentOperationContext;
import storm.trident.tuple.TridentTuple;

import com.stream.storm.util.ContextManager;

public class StoreFilter implements Filter {
	private static final long serialVersionUID = 8349619319605307634L;
	private static final Log logger = LogFactory.getLog(StoreFilter.class);
	private static Calendar calendar;
	private static String lastDate;
	private static Map<String, String> maps = new HashMap<String, String>();
	private static ApplicationContext applicationContext = ContextManager.getContext();

	private static ThreadLocal<SimpleDateFormat> formatCache = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyyMMdd");
		};
	};

	// private IRedisService redisService;
	// private IStormService stormService;

	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map conf, TridentOperationContext context) {
	}

	@Override
	public void cleanup() {
	}

	@Override
	public boolean isKeep(TridentTuple input) {
		//TODO doserver
		return false;
	}

}