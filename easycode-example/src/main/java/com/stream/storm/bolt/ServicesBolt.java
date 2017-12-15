package com.stream.storm.bolt;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

//import com.pxene.dsp.archer.common.constant.BaseConstant;
//import com.pxene.dsp.archer.service.IRedisService;
//import com.pxene.dsp.archer.service.IStormService;
//import com.pxene.storm.counter.StoreFilter;
//import com.pxene.util.ContextManager;
//import com.wins.dsp.common.redis.JedisUtil;
//import com.wins.dsp.common.util.GlobalUtil;

public class ServicesBolt extends BaseRichBolt {
	private static final long serialVersionUID = 8349619319605307634L;
	private static final Log logger = LogFactory.getLog(ServicesBolt.class);
	private static Calendar calendar;
	private static String lastDate;
	private static Map<String, String> maps = new HashMap<String, String>();
//	private static ApplicationContext applicationContext = ContextManager.getContext();

	private static ThreadLocal<SimpleDateFormat> formatCache = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyyMMdd");
		};
	};

//	private IRedisService redisService;
//	private IStormService stormService;

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
//		this.redisService = (IRedisService) applicationContext.getBean("redisService");
//		this.stormService = (IStormService) applicationContext.getBean("stormService");
	}

	@Override
	public void execute(Tuple input) {
		logger.info("mylog:" + input);
		DateFormat sdf = formatCache.get();
		// 每天更新maps
		calendar = Calendar.getInstance();
		if (!sdf.format(calendar.getTime()).equals(lastDate)) {
			lastDate = sdf.format(calendar.getTime());
			maps.clear();
		}
		String string = input.getString(0);
		String[] split = string.split(",");
//		doCounterServices(split[0], split[1], split[2], split[3], Integer.parseInt(split[4]));

	}

//	private void doCounterServices(String mapid, String type, String category, String time, int count) {
//		try {
//			String mapinfo = maps.get(mapid);
//			if (mapinfo == null) {
//				mapinfo = JedisUtil.getStr("mapping_" + mapid);
//				if (mapinfo != null)
//					maps.put(mapid, mapinfo);
//			}
//
//			if (mapinfo != null) {
//				String[] mapArry = mapinfo.split(",");
//				int flag = 0;
//				if (JedisUtil.getStr("budget_" + category + "_" + mapArry[0]) == null)
//					flag = 1;
//				// mysql insert counter record
//				insertMotionRecord(count, type, mapid, time, Integer.parseInt(category), flag);
//				String paytype = null;
//				if (maps.containsKey(mapArry[0] + "_paytype")) {
//					paytype = maps.get(mapArry[0] + "_paytype");
//				} else {
//					paytype = stormService.getCampaignPaytype(mapArry[0]);
//					maps.put(mapArry[0] + "_paytype", paytype);
//				}
//
//				if (paytype == null || !paytype.equals("01")) {
//					// 如果是按点击计费， 在此计费 mapArry==4 为点击计费 mapArry==3 为展现计费
//					if (mapArry.length == 4 && "c".equals(type)) {
//						chargeForThisResult(mapArry[0], mapArry[2], new BigDecimal(mapArry[3]).multiply(new BigDecimal(count)), category);
//					}
//					if (mapArry.length == 3 && "m".equals(type)) {
//						BigDecimal bid = null;
//						if (maps.containsKey(mapArry[2] + "_bid")) {
//							String bidStr = maps.get(mapArry[2] + "_bid");
//							bid = new BigDecimal(bidStr);
//						} else {
//							bid = stormService.getGroupBid(mapArry[2]);
//							bid = bid.multiply(BaseConstant.HUNDRED);
//							maps.put(mapArry[2] + "_bid", bid.toString());
//						}
//						chargeForThisResult(mapArry[0], mapArry[2], bid.multiply(new BigDecimal(count)).divide(BaseConstant.THOUSAND), category);
//					}
//				}
//				// counter control
//				counterControlForThisSumResult(mapArry[2], count, type, category, mapArry[0]);
//			} else {
//				logger.warn("******counter control error :" + mapid + " do not have a group");
//			}
//		} catch (NumberFormatException e) {
//			logger.error("******insert motion record error", e);
//		} catch (Exception e) {
//			logger.error("******insert motion record error", e);
//		}
//	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {

	}

	/**
	 * 按点击计费
	 * 
	 * @param campaignid
	 * @param groupid
	 * @param cost
	 */
//	private synchronized void chargeForThisResult(String campaignid, String groupid, BigDecimal cost, String category) {
//
//		String dsp_key = "dsp_budget_" + category + "_" + groupid;
//		String groupBudgetStr = JedisUtil.getStrWithLock(dsp_key);
//		if (groupBudgetStr != null) {
//			BigDecimal budget = new BigDecimal(groupBudgetStr);
//			budget = budget.subtract(cost);
//			if (budget.compareTo(new BigDecimal(0)) <= 0) {
//				JedisUtil.delete(dsp_key);
//				JedisUtil.unlock(dsp_key);
//				// 余额不足,停止该项目的投放
//				if (!stopGroup(groupid, category, campaignid)) {
//					GlobalUtil.addGroupidToOutOfBudgetListInRedis(groupid);
//					logger.info("***out of group budget, begin to stop the group " + groupid);
//				}
//			} else {
//				JedisUtil.setWithLock(dsp_key, budget.toString());
//			}
//		} else {
//			if (!stopGroup(groupid, category, campaignid)) {
//				GlobalUtil.addGroupidToOutOfBudgetListInRedis(groupid);
//				logger.info("***can not get the group budget, begin to stop the group " + groupid);
//			}
//		}
//	}

	/**
	 * 曝光、点击记录入库
	 * 
	 * @param count
	 * @param type
	 * @param mapid
	 * @param time
	 * @param category
	 */
//	private synchronized void insertMotionRecord(int count, String type, String mapid, String time, int category, int flag) {
//		stormService.insertOrUpdateHourCount(time, mapid, category, type, count, flag);
//	}

	/**
	 * 投放次数控制
	 * 
	 * @param groupid
	 * @param count
	 * @param type
	 */
//	private synchronized void counterControlForThisSumResult(String groupid, int count, String type, String category, String campaignid) {
//		String kpi_key = "dsp_counter_" + category + "_" + groupid;
//		String controlStr = JedisUtil.getStrWithLock(kpi_key);
//		if (controlStr != null) {
//			String imps, clks;
//			if (controlStr.startsWith(",")) {
//				imps = "";
//				clks = controlStr.split(",")[1];
//			} else if (controlStr.endsWith(",")) {
//				imps = controlStr.split(",")[0];
//				clks = "";
//			} else {
//				imps = controlStr.split(",")[0];
//				clks = controlStr.split(",")[1];
//			}
//
//			if ("m".equals(type) && !"".equals(imps)) {
//				int impressions = Integer.parseInt(imps) - count;
//				if (impressions > 0) {
//					JedisUtil.setWithLock(kpi_key, impressions + "," + clks);
//					System.out.println("-----redis-update");
//				} else {
//					if ("".equals(clks)) {
//						JedisUtil.delete(kpi_key);
//						JedisUtil.unlock(kpi_key);
//						if (!stopGroup(groupid, category, campaignid)) {
//							GlobalUtil.addCounterOutOfControlFlagToRedis(groupid);
//							logger.info("***out of impressions counter, begin to stop the group " + groupid);
//						}
//					} else {
//						JedisUtil.setWithLock(kpi_key, "," + clks);
//					}
//				}
//			} else if ("c".equals(type) && !"".equals(clks)) {
//				int clicks = Integer.parseInt(clks) - count;
//				if (clicks > 0) {
//					JedisUtil.setWithLock(kpi_key, imps + "," + clicks);
//				} else {
//					if ("".equals(imps)) {
//						JedisUtil.delete(kpi_key);
//						JedisUtil.unlock(kpi_key);
//						if (!stopGroup(groupid, category, campaignid)) {
//							GlobalUtil.addCounterOutOfControlFlagToRedis(groupid);
//							logger.info("***out of clicks counter, begin to stop the group " + groupid);
//						}
//					} else {
//						JedisUtil.setWithLock(kpi_key, imps + ",");
//					}
//				}
//			} else {
//				JedisUtil.setWithLock(kpi_key, controlStr);
//			}
//		}
//	}

	/**
	 * 停止项目投放
	 * 
	 * @param groupid
	 */
//	private boolean stopGroup(String groupid, String category, String campaignid) {
//		synchronized (groupid) {
//			if (JedisUtil.exists(BaseConstant.TEMPLATE_DELETING_KEY)) {
//				return true;
//			}
//			boolean flag = stormService.preStopGroup(groupid, category);
//			if (!flag) {
//				redisService.removeMapidInCreativeSizeByGroupid(groupid);
//
//				// this.stopCampaign(campaignid, category);
//			}
//			return flag;
//		}
//	}
}
