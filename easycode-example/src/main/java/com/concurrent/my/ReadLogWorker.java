package com.concurrent.my;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadLogWorker extends Thread {

	public BufferedReader bufferedReader;
	private String reg;
	private CountDownLatch countDownLatch;// 计数器

	public ReadLogWorker(BufferedReader bufferedReader, String reg, CountDownLatch countDownLatch) {
		super();
		this.bufferedReader = bufferedReader;
		this.reg = reg;
		this.countDownLatch = countDownLatch;
	}

	public void run() {
		try {
			//读取一行的值
			String temp;
			while ((temp = bufferedReader.readLine()) != null) {
				
				// 匹配字符串
				ArrayList<String> strings = getStrings(reg, temp);

				// 匹配上了
				if (strings != null && strings.size() != 0) {
					ResultList.addResult(strings);
				}
			}
			countDownLatch.countDown();
		}   catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 匹配字符串
	 * 
	 * @param reg
	 * @param str
	 * @return
	 */
	private static ArrayList<String> getStrings(String reg, String str) {
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(str);
		ArrayList<String> strs = new ArrayList<String>();
		while (m.find()) {
			strs.add(m.group(0));
		}
		return strs;
	}
	
	

}
