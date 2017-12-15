package com.encrypt;

import java.util.HashMap;
import java.util.Map;

public class TestAdv {

	public static void main(String[] args) throws Exception {
//		String path = "http://192.168.3.166:8080/dsp-admin/advertiserManager.do?action=selectAdvertiser";
//		String path = "http://127.0.0.1:8080/dsp-admin/advertiserManager.do?action=addAdvertiser";
		String path = "http://127.0.0.1:8080/dsp-admin/baseAdertiser.do?action=createAdvertiser";
		
		Map<String, String> postParam = new HashMap<String, String>();
//		postParam.put("millis", millis + "");

//		String a ="abc={\"appid\":\""+appid+"\",\"lol\":\""+lol+"\",\"signature\":\""+signature+"\",\"millis\":\""+millis+"\",\"content\""+content+"\" } ";
		
		

		String sendPostParam = HttpClientUtil.sendPostParam(path, postParam);//获得数据并且发送请求
		System.out.println(sendPostParam);

	}
}
