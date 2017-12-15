package com.requestParse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.http.protocol.HTTP;

 

public class ReadHttpRequestInputStream {

	
	

	public String getParserRequestToBean(HttpServletRequest request) {
		try {
			//获取流
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String line = null;
			//得到内容
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			// 将资料解码
			String reqBody = sb.toString();
			String decode = URLDecoder.decode(reqBody, HTTP.UTF_8);
			
			//转换成json对象
			return "";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
			
		}
	}
}
