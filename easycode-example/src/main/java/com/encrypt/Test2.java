package com.encrypt;
import java.util.HashMap;
import java.util.Map;

import org.bouncycastle.util.encoders.Base64;

 

public class Test2 {
//	public static String URL = "http://127.0.0.1:8080/dsp-open/opendsp.do/service";
	public static String URL = "http://127.0.0.1:8080/dsp-open/pxene/opendsp.do";
	public static void main(String[] args) throws Exception {
		String[] methodStrings ={  "getAdvertiser", "createAdvertiser",
				"editAdvertiser",//2
				"delAdvertiser",//3
				"addAdvertiserAptitudes",//4
				"advRecharge",//5
				"getAdvertiserBalance",//6
				"createCampaign",//7
				"delCampaignByid",//8
				"editCampaign",//9
				"getCampaignById",//10
				"getCampaignList",//11
				"addSource",//12
				"delSource",//13
				"createCreative",//14 
				"editCreative",//15
				"delCreative",//16
				"getCreative"//17
		};
		String sendPostParam = HttpClientUtil.sendPostJSONData(URL, "");//获得数据并且发送请求
//		String sendPostParam = HttpClientUtil.sendPokstParam(URL, getPostParam(methodStrings[10]));//获得数据并且发送请求
//		String data = getData(sendPostParam); 
System.out.println(sendPostParam);
//		System.out.println(data);
 
	}

	//解密
	public static String getData(String encryptString) throws Exception {
		String key = "adjdjfjfjfjdkdkd";//
		byte[] decode = Base64.decode(encryptString.getBytes());
		String aString = new String(decode, "utf-8");
		String decrypt = AESTool.decrypt(aString, key);
		return decrypt;
	}

	//不用管
	public static Map<String, String> getPostParam(String content) throws Exception {
		Map<String, String> postParam = new HashMap<String, String>();

		content = ParamClass.getContent(content);
		String appid = "t123456";// 用户
		String token = "cst123456";// 令牌
		String key = "adjdjfjfjfjdkdkd";//
		// 业务数据
		long millis = System.currentTimeMillis();// 时间戳j
		content = AESTool.encrypt(content, key);// 使用aes加密
		String lol = SignatureUtil.digest(content, "MD5");// 摘要
		String signature = SignatureUtil.generateSignature(appid, token, lol, millis);// 签名

		// 准备提交数据
		postParam.put("appid", appid);
		postParam.put("content", content);
		postParam.put("lol", lol);
		postParam.put("signature", signature);
		postParam.put("millis", millis + "");

//		String a ="abc={\"appid\":\""+appid+"\",\"lol\":\""+lol+"\",\"signature\":\""+signature+"\",\"millis\":\""+millis+"\",\"content\""+content+"\" } ";
		
		return postParam;
	}
	 
}
