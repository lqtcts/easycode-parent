package com.encrypt;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.util.encoders.Base64;

 

public class Test {

	public static void main(String[] args) throws Exception {
		byte[] encode = Base64.encode("132456".getBytes());
		System.out.println(encode[1]);
		
		
		String path = "http://127.0.0.1:8080/dsp-open//opendsp.do";

		BufferedReader in = null;
		try {
			// 定义HttpClient
			@SuppressWarnings({ "deprecation", "resource" })
			HttpClient client = new DefaultHttpClient();
			// 实例化HTTP方法
			HttpPost request = new HttpPost(path);
			// 创建名/值组列表
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();

			String appid = "t123456";// 用户
			String token = "cst123456";// 令牌
			String key = "adjdjfjfjfjdkdkd";// 
			//业务数据
			//查询
			String content = "{\"servicename\":\"advertiserServiceCall\", \"funcname\": \"getAdvertiserByid\", \"methodparam\":{ \"advertiserid\":\"1852090d-fc6d-4e60-aaad-949d10b0b4c6\" } }";
			//增加
//			String content = "{\"servicename\":\"advertiserServiceCall\",\"funcname\":\"createAdvertiser\",\"methodparam\":{\"name\":\"111\",\"passwd\":\"qqqqqqqqq\",\"company\":\"111\",\"contacts\":\"11\",\"contactsphone\":\"13111111111\",\"coefficient\":\"111\",\"phone\":\"222222\",\"email\":\"1111111@qq.com\",\"address\":\"222\",\"category\":\"2\",\"seller\":\"22\",\"usertype\":\"2\",\"zip\":\"222222\",\"domain\":\"2222\",\"sitename\":\"22\",\"organizationno\":\"22\"}}";
			long millis = System.currentTimeMillis();//时间戳j
			content=AESTool.encrypt(content, key);//使用aes加密
			String lol = SignatureUtil.digest(content, "MD5");// 摘要
			String signature = SignatureUtil.generateSignature(appid, token, lol, millis);// 签名

			//准备提交数据
			parameters.add(new BasicNameValuePair("appid",appid));
			parameters.add(new BasicNameValuePair("content",content));
			parameters.add(new BasicNameValuePair("lol",lol));
			parameters.add(new BasicNameValuePair("signature",signature));
			parameters.add(new BasicNameValuePair("millis",millis+""));
			
			// 创建UrlEncodedFormEntity对象
			UrlEncodedFormEntity formEntiry = new UrlEncodedFormEntity(parameters);
			request.setEntity(formEntiry);
			
//			
//			HashMap<String, String> headers = new HashMap<String, String>();  
//			headers.put("Referer", "");  
//			headers.put("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.6) Gecko/20100625  Firefox/3.6.6 Greatwqs");  
//			headers.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");  
//			headers.put("Accept-Language","zh-cn,zh;q=0.5");  
//			headers.put("Host","www.yourdomain.com");  
//			headers.put("Accept-Charset","ISO-8859-1,utf-8;q=0.7,*;q=0.7");  
//			headers.put("Referer", "http://www.yourdomian.com/xxxAction.html");  
//			
			
//			request.setHeader("Content-Type", "application/json");  
//			request.addHeader("Accept", "application/json");  
			
			
			// 执行请求
			HttpResponse response = client.execute(request);
			
//			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String string = EntityUtils.toString(response.getEntity());
			System.out.println(string);
//			StringBuffer sb = new StringBuffer("");
//			String line = "";
//			String NL = System.getProperty("line.separator");
//			while ((line = in.readLine()) != null) {
//				sb.append(line + NL);
//			}
//			in.close();
//			String result = sb.toString();
//			System.out.println("-------------------------------------");
//			System.out.println(result);
//			byte[] decode = Base64.decode(result.getBytes());
//			String aString = new String(decode);
//			System.out.println(AESTool.decrypt(aString,key));

		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
