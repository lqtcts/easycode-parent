//package com.util;
//
//import com.google.gson.Gson;
//import com.modal.AlarmModal;
//import com.modal.AlarmModalResult;
//import com.util.ESRepository;
//import com.util.FuncUtil;
//import com.util.PubPara;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.common.unit.TimeValue;
//import org.elasticsearch.index.query.BoolQueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.SearchHit;
//import org.elasticsearch.search.aggregations.AggregationBuilder;
//import org.elasticsearch.search.aggregations.AggregationBuilders;
//import org.elasticsearch.search.aggregations.bucket.terms.Terms;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class AlarmDataServer {
////	public static void main(String[] args) {
////		AlarmDataServer alarmDataServer = new AlarmDataServer();
////		System.out.println(alarmDataServer.findConstantlyAlarm());
////	}
//	/**
//	 * 查询实时告警信息
//	 * @return
//	 */
//	public String findConstantlyAlarm(HttpServletRequest request){
//		AlarmModalResult result = null;
//
//		try {
////			Date date = new Date();
////			Calendar cal = Calendar.getInstance();
////			cal.setTime(date);
////			cal.set(Calendar.HOUR_OF_DAY, 0);
////			cal.set(Calendar.SECOND, 0);
////			cal.set(Calendar.MINUTE, 0);
////			long lStartTime = cal.getTimeInMillis()/1000;
////			long lEndTime = date.getTime() / 1000;
////			long lEndTime = System.currentTimeMillis()/1000;
//			String strTime = FuncUtil.Long2StrTime(System.currentTimeMillis(), "yyyyMMdd");
//
//			BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();	 //建立查询类
////			queryBuilder.must(QueryBuilders.rangeQuery("time").gte(lStartTime).lte(lEndTime));
//			AggregationBuilder aggreation = AggregationBuilders.terms("aggs").field("strCity").size(10000);
//			SearchResponse response = ESRepository.client.prepareSearch(PubPara.ES_INDEX_NAME_KA+strTime).setTypes(PubPara.ES_TYPE_NAMES_KA_jingxin)
//					.setQuery(queryBuilder)
//					.addAggregation(aggreation)
//					.setScroll(new TimeValue(1000*30))				//设置超时时间
//					.setSize(30000)
//			        .execute().actionGet();
//
//			Map<String,List<String>> alarmImsiMap = new HashMap<>();
//			for (SearchHit hit : response.getHits()) {
//				try {
//					Map<String, Object> source = hit.getSource();
//					String tempArray[] = source.get("strCity").toString().split(" ");
//					String strCityName = "";
//					if(tempArray.length == 2){
//						if("新疆".equals(tempArray[0]) || "西藏".equals(tempArray[0])){
//							strCityName = tempArray[1];
//						}else{
//							strCityName = tempArray[0];
//						}
//					}else{
//						strCityName = tempArray[0];
//					}
//					String strImsi = source.get("strImsi").toString();
//					if(!FuncUtil.isNull(strImsi)){
//						strImsi = strImsi.substring(0,9)+"*****";
//					}
//					List<String> imsiArray = null;
//					if(alarmImsiMap.containsKey(strCityName)){
//						imsiArray = alarmImsiMap.get(strCityName);
//						if(!imsiArray.contains(strImsi)){
//							if(imsiArray.size() > 11){
//								imsiArray.remove(0);
//							}
//							imsiArray.add(strImsi);
//							alarmImsiMap.put(strCityName, imsiArray);
//						}
//					}else{
//						imsiArray = new ArrayList<>();
//						imsiArray.add(strImsi);
//						alarmImsiMap.put(strCityName, imsiArray);
//					}
//
//				} catch (Exception e) {
//					PubPara.logger.error(e);
//				}
//			}
//
//			Map<String,Integer> imsiNumberMap = new HashMap<>();
//			Terms agg = response.getAggregations().get("aggs");
//			for (int i = 0;i< agg.getBuckets().size() ; i++) {
//				try {
//					String strCityName = agg.getBuckets().get(i).getKeyAsString();
//					if("".equals(strCityName)){
//						continue;
//					}
//					String[] tempArray = strCityName.split(" ");
//					int lCountTemp = (int)agg.getBuckets().get(i).getDocCount();
//					if(tempArray.length == 2){
//						if("新疆".equals(tempArray[0]) || "西藏".equals(tempArray[0])){
//							strCityName = tempArray[1];
//						}else{
//							strCityName = tempArray[0];
//						}
//					}else{
//						strCityName = tempArray[0];
//					}
//					if(imsiNumberMap.containsKey(strCityName)){
//						int tempNum = imsiNumberMap.get(strCityName);
//						imsiNumberMap.put(strCityName, lCountTemp+tempNum);
//					}else{
//						imsiNumberMap.put(strCityName, lCountTemp);
//					}
//
//				} catch (Exception e) {
////						// TODO Auto-generated catch block
////						e.printStackTrace();
//				}
//			}
//
//			List<AlarmModal> alarmModalList = new ArrayList<>();
//			for(String strKey : alarmImsiMap.keySet()){
//				AlarmModal alarmModal = new AlarmModal();
//				alarmModal.setStrCity(strKey);
//				alarmModal.setAlarmImsiList(alarmImsiMap.get(strKey));
//				if(imsiNumberMap.containsKey(strKey)){
//					alarmModal.setHistoryCount(imsiNumberMap.get(strKey));
//				}
//				alarmModalList.add(alarmModal);
//			}
//			result = new AlarmModalResult("ok","", alarmModalList);
//		} catch (Exception e) {
//			e.printStackTrace();
//			result = new AlarmModalResult("error",e.getMessage(), new ArrayList<AlarmModal>());
//		}
//
//		return (new Gson()).toJson(result);
//	}
//}
