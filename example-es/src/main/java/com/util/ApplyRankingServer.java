//package com.util;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.common.unit.TimeValue;
//import org.elasticsearch.index.query.BoolQueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.aggregations.AggregationBuilder;
//import org.elasticsearch.search.aggregations.AggregationBuilders;
//import org.elasticsearch.search.aggregations.bucket.terms.Terms;
//import org.elasticsearch.search.builder.SearchSourceBuilder;
//
//import com.google.gson.Gson;
//import com.modal.ApplyRankingChart;
//import com.modal.ApplyRankingResult;
//import com.util.ESRepository;
//import com.util.FuncUtil;
//import com.util.PubIdentityMessage;
//import com.util.PubPara;
//
//public class ApplyRankingServer {
//	/**
//	 * 初始加载数据
//	 * @param request
//	 * @return
//	 */
//	public String applyRankingChartInit(HttpServletRequest request){
//		ApplyRankingResult arResult = null;
//		try {
//			BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();	 //建立查询类
//			AggregationBuilder aggreation = AggregationBuilders.terms("aggs").field("strIdType").size(10000);
//			String strTime = FuncUtil.Long2StrTime(System.currentTimeMillis(), "yyyyMMdd");
////			SearchSourceBuilder ssb = new SearchSourceBuilder();
////			String jsons = ssb.aggregation(aggreation).toString();
////			System.out.println("ES-->DSL::"+jsons);
//			SearchResponse response = ESRepository.client.prepareSearch(PubPara.ES_INDEX_NAME_KA+strTime).setTypes(PubPara.ES_TYPE_NAMES_KA_tmac)
//					.setQuery(queryBuilder)
//					.addAggregation(aggreation)
//					.setScroll(new TimeValue(1000*30))				//设置超时时间
//					.setFrom(0).setSize(1)
//			        .execute().actionGet();
////				SearchSourceBuilder findJson = new SearchSourceBuilder();
////				System.out.println(findJson.query(queryBuilder).toString());
//			List<ApplyRankingChart> arcList = new ArrayList<>();
//			Terms agg = response.getAggregations().get("aggs");
//			for (int i = 0;i< agg.getBuckets().size() ; i++) {
//				if(arcList.size() >= 12){
//					continue;
//				}
//				try {
//					String strIdTypeTemp = agg.getBuckets().get(i).getKeyAsString();
//					if("".equals(strIdTypeTemp) || "0".equals(strIdTypeTemp)){
//						continue;
//					}
//					int lCountTemp = (int)agg.getBuckets().get(i).getDocCount();
//					ApplyRankingChart applyRankingChart = new ApplyRankingChart();
//					applyRankingChart.setnCount(lCountTemp);
//					applyRankingChart.setStrIdType(strIdTypeTemp);
//					applyRankingChart.setStrIdContent(PubIdentityMessage.getIdentityByType(Integer.valueOf(strIdTypeTemp)));
//					arcList.add(applyRankingChart);
//				} catch (Exception e) {
////						// TODO Auto-generated catch block
////						e.printStackTrace();
//				}
//			}
//			arResult = new ApplyRankingResult("ok", "",arcList);
//		} catch (Exception e) {
//			e.printStackTrace();
//			arResult = new ApplyRankingResult("error", "",new ArrayList<ApplyRankingChart>());
//		}
//
//		return (new Gson()).toJson(arResult);
//	}
//
//	public String findARCInterval(HttpServletRequest request){
//		ApplyRankingResult arResult = null;
//		try {
//			int findTime = Integer.valueOf(request.getParameter("intervalTimeARC"));
//			if(findTime >= 60*60*12){
//				arResult = new ApplyRankingResult("error", "intervalTimeARC is too big",new ArrayList<ApplyRankingChart>());
//			}else{
//				long lEndTime = System.currentTimeMillis()/1000;
//				long lStartTime = lEndTime - findTime;
//				String strEndTime = FuncUtil.Long2StrTime(lEndTime*1000, "yyyyMMdd");
//				String strStartTime = FuncUtil.Long2StrTime(lStartTime*1000, "yyyyMMdd");
//				String[] strIndexAry = null;
//				strIndexAry = new String[]{PubPara.ES_INDEX_NAME_KA+strEndTime};
//				if(!strStartTime.equals(strEndTime)){
//					strIndexAry = new String[]{PubPara.ES_INDEX_NAME_KA+strStartTime,PubPara.ES_INDEX_NAME_KA+strEndTime};
//				}
////				String[] strTypeAry = new String[]{PubPara.ES_TYPE_NAMES_KA_tmac,PubPara.ES_TYPE_NAMES_KA_jingxin};
//				BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();	 //建立查询类
//				AggregationBuilder aggreation = AggregationBuilders.terms("aggs").field("strIdType").size(10000);
//				SearchSourceBuilder ssb = new SearchSourceBuilder();
//				String jsons = ssb.aggregation(aggreation).toString();
//				PubPara.logger.info("ES-->DSL::"+jsons);
//				SearchResponse response = ESRepository.client.prepareSearch(strIndexAry)
//						.setQuery(queryBuilder)
//						.addAggregation(aggreation)
//						.setScroll(new TimeValue(1000*30))				//设置超时时间
//						.setFrom(0).setSize(1)
//				        .execute().actionGet();
////				SearchSourceBuilder findJson = new SearchSourceBuilder();
////				System.out.println(findJson.query(queryBuilder).toString());
//				List<ApplyRankingChart> arcList = new ArrayList<>();
//				Terms agg = response.getAggregations().get("aggs");
//				for (int i = 0;i< agg.getBuckets().size() ; i++) {
//					try {
//						String strIdTypeTemp = (String)agg.getBuckets().get(i).getKey();
//						int lCountTemp = (int)agg.getBuckets().get(i).getDocCount();
//						ApplyRankingChart applyRankingChart = new ApplyRankingChart();
//						applyRankingChart.setnCount(lCountTemp);
//						applyRankingChart.setStrIdType(strIdTypeTemp);
//						applyRankingChart.setStrIdContent(PubIdentityMessage.getIdentityByType(Integer.valueOf(strIdTypeTemp)));
//						arcList.add(applyRankingChart);
//					} catch (Exception e) {
////						// TODO Auto-generated catch block
////						e.printStackTrace();
//					}
//				}
//				arResult = new ApplyRankingResult("ok", "",arcList);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			arResult = new ApplyRankingResult("error", "",new ArrayList<ApplyRankingChart>());
//		}
//
//		return (new Gson()).toJson(arResult);
//	}
//}
