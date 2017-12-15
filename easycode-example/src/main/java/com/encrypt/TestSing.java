package com.encrypt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestSing {
	
	public static void main(String[] args) {
		List<String> srcList = new ArrayList<String>();
		String appid="abc123";
		String token="f1---23";
		String lol="e123";
		long millis=System.currentTimeMillis();
		srcList.add(millis+"");
		srcList.add(appid);
		srcList.add(token);
		srcList.add(lol);
		// 按照字典序逆序拼接参数
		Collections.sort(srcList);
		Collections.reverse(srcList);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < srcList.size(); i++) {
			sb.append(srcList.get(i));
		}
		System.out.println(sb.toString());
		System.out.println(SignatureUtil.digest(sb.toString(), "SHA-1"));
		String generateSignature = SignatureUtil.generateSignature(appid, token, lol, millis);
		System.out.println(generateSignature);
		
	}

}
