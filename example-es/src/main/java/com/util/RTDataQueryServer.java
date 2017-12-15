/*
package com.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import com.google.gson.Gson;
import com.modal.ImsiModal;
import com.modal.MacModal;
import com.modal.RtImsiResult;
import com.modal.RtMacResult;

import com.util.ESRepository;
import com.util.FuncUtil;

import com.util.PageClass;
import com.util.PubIdentityMessage;
import com.util.PubPara;

public class RTDataQueryServer {

	*/
/**
	 * mac查询
	 * @param request
	 * @return
	 *//*

	public String findMac(HttpServletRequest request){
		RtMacResult strResult = null;
		List<MacModal> macList = new ArrayList<>();
		MacModal searchDao = new MacModal();
		PageClass page = new PageClass();
		try {
			String strTmac = request.getParameter("strTmac");
			String strIdType = request.getParameter("strIdType");
			String strSSid = request.getParameter("strSSid");
			String strApMac = request.getParameter("strApMac");
			String strStartTime = request.getParameter("strStartTime");
			String strEndTime = request.getParameter("strEndTime");
			searchDao.setStrTmac(strTmac);
			searchDao.setStrApSsid(strSSid);
			searchDao.setStrApMac(strApMac);
			searchDao.setSearchStartTime(strStartTime);
			searchDao.setSearchEndTime(strEndTime);
			searchDao.setStrIdType(strIdType);

			int nowPage = Integer.valueOf(request.getParameter("nowPage"));

			BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();	 //建立查询类

			if(!FuncUtil.isNull(strTmac)){
				queryBuilder.must(QueryBuilders.wildcardQuery("strTmac", "*"+strTmac+"*"));
			}
			if(!FuncUtil.isNull(strIdType) && !"0".equals(strIdType)){
				queryBuilder.must(QueryBuilders.termQuery("strIdType", strIdType));
			}
			if(!FuncUtil.isNull(strSSid)){
				queryBuilder.must(QueryBuilders.wildcardQuery("strApSsid", "*"+strSSid+"*"));
			}
			if(!FuncUtil.isNull(strApMac)){
				queryBuilder.must(QueryBuilders.wildcardQuery("strApMac", "*"+strApMac+"*"));
			}
			  //yyyy-MM-dd HH:mm
			long lStartTime = FuncUtil.StrTimeToLong(strStartTime, "yyyy-MM-dd HH:mm");
			long lEndTime =   FuncUtil.StrTimeToLong(strEndTime, "yyyy-MM-dd HH:mm");
			//日期
			if(lStartTime > 0 && lStartTime < lEndTime){
				queryBuilder.must(QueryBuilders.rangeQuery("time").gte(lStartTime).lte(lEndTime));
			}else{
				PubPara.logger.error("error: time is error");
				strResult = new RtMacResult("error","time is error", macList);
				return (new Gson()).toJson(strResult);
			}
			//得到查询的Index
			String strIndex = FuncUtil.getDays(lStartTime,lEndTime,"yyyyMMdd",PubPara.ES_INDEX_NAME_KA);
			strIndex = FuncUtil.checkIndexES(strIndex);
			if(FuncUtil.isNull(strIndex)){
				PubPara.logger.error("error: Index  is null");
				strResult = new RtMacResult("error","Index is null", macList);
				return (new Gson()).toJson(strResult);
			}
			String[] strIndexAry = strIndex.split(",");
			//将查询的DSL语句村到session中
			SearchSourceBuilder aa = new SearchSourceBuilder();
			String jsons = aa.query(queryBuilder).toString();
			PubPara.logger.info("ES-->DSL::"+jsons);
			//查询
			SearchResponse response = ESRepository.client.prepareSearch(strIndexAry).setTypes(PubPara.ES_TYPE_NAMES_KA_tmac)
					.setQuery(queryBuilder)
					.addSort("time", SortOrder.DESC)
					.setFrom((nowPage-1)*50)
					.setSize(50)
					.setExplain(false)								//设置是否按查询匹配度排序
					.execute()
					.actionGet();

			SearchHits hits = response.getHits();
			int count = (int)hits.getTotalHits();
			for (SearchHit hit : response.getHits()) {
				Map<String, Object> source = hit.getSource();
				MacModal macDao = new MacModal();
				try {
					String strShowTMac = source.get("strTmac").toString();
					if(!FuncUtil.isNull(strShowTMac)){
						strShowTMac = strShowTMac.substring(0, 8)+"*****";
						macDao.setStrTmac(strShowTMac);
					}else{
						macDao.setStrTmac("");
					}
					String strShowApMac = source.get("strApMac").toString();
					if(!FuncUtil.isNull(strShowApMac)){
						strShowApMac = strShowApMac.substring(0, 8)+"*****";
						macDao.setStrApMac(strShowApMac);
					}else{
						macDao.setStrApMac("");
					}
//					macDao.setStrTmac(source.get("strTmac").toString());
//					macDao.setStrApMac(source.get("strApMac").toString());
					macDao.setStrApSsid(source.get("strApSsid").toString());
					macDao.setStrDeviceCode(source.get("strDeviceCode").toString());
					macDao.setStrDeviceName(PubPara.deviceMap.get(source.get("strDeviceCode").toString()));
					macDao.setStrIdType(source.get("strIdType").toString());
					macDao.setStrIdName(PubIdentityMessage.getIdentityByType(Integer.valueOf(source.get("strIdType").toString())));
					String strShowIdContent = source.get("strIdContent").toString();
					if(!FuncUtil.isNull(strShowIdContent)){
						if(strShowIdContent.length() > 6){
							strShowIdContent = strShowIdContent.substring(0,6)+"*****";
						}
						macDao.setStrShowIdContent(strShowIdContent);
					}else{
						macDao.setStrShowIdContent("");
					}

					macDao.setStrPlaceCode(source.get("strPlaceCode").toString());
					macDao.setlTime(Long.valueOf(source.get("time").toString()));
					macDao.setStrTime(FuncUtil.Long2StrTime(macDao.getlTime()*1000, "yyyy-MM-dd HH:mm:ss"));
//					macDao.setStrCity(source.get("strCity").toString());
//					macDao.setStrCityCode(source.get("strCityCode").toString());
					macList.add(macDao);
				} catch (Exception e) {
					PubPara.logger.error("Error :"+e);
				}
			}

			page.setNowPage(nowPage);
			page.setCount(count);
			page.setPageCount(page.getCount()%page.getPageNum() == 0? page.getCount()/page.getPageNum() :page.getCount()/page.getPageNum()+1);
			page.getPageNums();

			strResult = new RtMacResult("ok","", macList);
			strResult.setPageDao(page);
			strResult.setSearchDao(searchDao);
		} catch (Exception e) {
			PubPara.logger.error("Error :"+e);
			strResult = new RtMacResult("error",e.getMessage(), null);
			return (new Gson()).toJson(strResult);
		}
		return (new Gson()).toJson(strResult);
	}


	*/
/**
	 * imsi查询
	 * @param request
	 * @return
	 *//*

	public String findImsi(HttpServletRequest request){
		RtImsiResult strResult = null;
		List<ImsiModal> imsiList = new ArrayList<>();
		ImsiModal searchDao = new ImsiModal();
		PageClass page = new PageClass();
		try {
			String strImsi = request.getParameter("strImsi");
			String strImei = request.getParameter("strImei");
			String strCity = request.getParameter("strCity");
			String strStartTime = request.getParameter("strStartTime");
			String strEndTime = request.getParameter("strEndTime");
			searchDao.setStrImsi(strImsi);
			searchDao.setStrImei(strImei);
			searchDao.setSearchStartTime(strStartTime);
			searchDao.setSearchEndTime(strEndTime);
			searchDao.setStrCityCode(strCity);

			int nowPage = Integer.valueOf(request.getParameter("nowPage"));

			BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();	 //建立查询类

			if(!FuncUtil.isNull(strImsi)){
				queryBuilder.must(QueryBuilders.wildcardQuery("strImsi", "*"+strImsi+"*"));
			}
			if(!FuncUtil.isNull(strImei)){
				queryBuilder.must(QueryBuilders.wildcardQuery("strImei", "*"+strImei+"*"));
			}
			if(!FuncUtil.isNull(strCity) && !"0".equals(strCity)){
				queryBuilder.must(QueryBuilders.wildcardQuery("strCityCode", strCity+"*"));
			}
			  //yyyy-MM-dd HH:mm
			long lStartTime = FuncUtil.StrTimeToLong(strStartTime, "yyyy-MM-dd HH:mm");
			long lEndTime =   FuncUtil.StrTimeToLong(strEndTime, "yyyy-MM-dd HH:mm");
			//日期
			if(lStartTime > 0 && lStartTime < lEndTime){
				queryBuilder.must(QueryBuilders.rangeQuery("time").gte(lStartTime).lte(lEndTime));
			}else{
				PubPara.logger.error("error: time is error");
				strResult = new RtImsiResult("error","time is error", imsiList);
				return (new Gson()).toJson(strResult);
			}
			//得到查询的Index
			String strIndex = FuncUtil.getDays(lStartTime,lEndTime,"yyyyMMdd",PubPara.ES_INDEX_NAME_KA);
			strIndex = FuncUtil.checkIndexES(strIndex);
			if(FuncUtil.isNull(strIndex)){
				PubPara.logger.error("error: Index  is null");
				strResult = new RtImsiResult("error","time is error", imsiList);
				return (new Gson()).toJson(strResult);
			}
			String[] strIndexAry = strIndex.split(",");
			//将查询的DSL语句村到session中
			SearchSourceBuilder aa = new SearchSourceBuilder();
			String jsons = aa.query(queryBuilder).toString();
			PubPara.logger.info("ES-->DSL::"+jsons);
			//查询
			SearchResponse response = ESRepository.client.prepareSearch(strIndexAry).setTypes(PubPara.ES_TYPE_NAMES_KA_jingxin)
					.setQuery(queryBuilder)
					.addSort("time", SortOrder.DESC)
					.setFrom((nowPage-1)*50)
					.setSize(50)
					.setExplain(false)								//设置是否按查询匹配度排序
					.execute()
					.actionGet();

			SearchHits hits = response.getHits();
			int count = (int)hits.getTotalHits();
			for (SearchHit hit : response.getHits()) {
				Map<String, Object> source = hit.getSource();
				ImsiModal imsiDao = new ImsiModal();
				try {
					String showImsi = source.get("strImsi").toString();
					String showImei = source.get("strImei").toString();
					String showPhone = source.get("strPhoneNum").toString();

					if(!FuncUtil.isNull(showImsi)){
						imsiDao.setStrShowImsi(showImsi.substring(0,9)+"*****");
					}else{
						imsiDao.setStrShowImsi("");
					}

					if(!FuncUtil.isNull(showImei)){
						imsiDao.setStrShowImei(showImei.substring(0,9)+"*****");
					}else{
						imsiDao.setStrShowImei("");
					}
					if(!FuncUtil.isNull(showPhone)){
						imsiDao.setStrPhone(showPhone.substring(0,5)+"*****");
						imsiDao.setStrCity(FuncUtil.isNull(source.get("strCity").toString()) ? "":source.get("strCity").toString());
					}else{
						imsiDao.setStrPhone("");
						imsiDao.setStrCity("");
					}
					imsiDao.setStrDeviceCode(source.get("strDeviceCode").toString());
					imsiDao.setStrDeviceName(PubPara.deviceMap.get(source.get("strDeviceCode").toString()));
					imsiDao.setlTime(Long.valueOf(source.get("time").toString()));
					imsiDao.setStrTime(FuncUtil.Long2StrTime(imsiDao.getlTime()*1000, "yyyy-MM-dd HH:mm:ss"));
					imsiDao.setStrCityCode(source.get("strCityCode").toString());
					imsiDao.setStrOperatorCode(source.get("strOperator").toString());
					imsiDao.setStrOperatorName(PubPara.operatorMap.get(source.get("strOperator").toString()));
					imsiList.add(imsiDao);
				} catch (Exception e) {
					PubPara.logger.error("Error :"+e);
				}
			}

			page.setNowPage(nowPage);
			page.setCount(count);
			page.setPageCount(page.getCount()%page.getPageNum() == 0? page.getCount()/page.getPageNum() :page.getCount()/page.getPageNum()+1);
			page.getPageNums();

			strResult = new RtImsiResult("ok","", imsiList);
			strResult.setPageDao(page);
			strResult.setSearchDao(searchDao);
		} catch (Exception e) {
			PubPara.logger.error("Error :"+e);
			strResult = new RtImsiResult("error","time is error", imsiList);
			return (new Gson()).toJson(strResult);
		}
		return (new Gson()).toJson(strResult);
	}


}
*/
