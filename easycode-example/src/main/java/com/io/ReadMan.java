package com.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import com.concurrent.my.ResultList;
import com.utils.DateUtil;
import com.yammer.metrics.core.HealthCheck.Result;

public class ReadMan {

	public static void main(String[] args) throws Exception {
		
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("ddd", "a");

		System.out.println(jsonObject.get("dddd"));
	}
	
	
	public void  name() throws NumberFormatException, IOException {
	       final String filePath = "C:\\Users\\wuchengbin\\Desktop\\linshi\\";  
	        String[] fStrings={"20160729\\004446"
	        		,"20160730\\083240"
	        		,"20160731\\041524"
	        		,"20160801\\002801"
	        		,"20160802\\062055"
	        };

			//读取一行的值
			for (int i = 0; i < fStrings.length; i++) {
				BufferedReader  bufferedReader = new BufferedReader(new FileReader(new File(filePath+fStrings[i])));
				String temp;
				int pv = 0,uv = 0,count=0;
				while ((temp = bufferedReader.readLine()) != null) {
					if (temp.endsWith("1")) {
						count++;
						String[] split = temp.split("\\|");
						String pvs = split[2];
						String uvs = split[3];
						pv=pv+Integer.parseInt(pvs);
						uv=uv+Integer.parseInt(uvs);
					}
				}
				System.out.println(fStrings[i]+"      pv:"+pv+" uv:"+uv);
			}
	}
	
	
	 
}
