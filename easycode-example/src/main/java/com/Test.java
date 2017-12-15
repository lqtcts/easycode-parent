package com;

import com.utils.HttpClientUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.apache.hadoop.yarn.webapp.hamlet.HamletSpec.InputType.file;

public class Test {

	public static void main(String[]  args) throws IOException {
//		String file="C:\\Users\\wuchengbin\\Desktop\\ba.txt";
//			BufferedReader  bufferedReader = new BufferedReader(new FileReader(new File(file)));
//			String temp;
//			int count=1;
//			while ((temp = bufferedReader.readLine()) != null) {
//					count++;
//					if(temp!=null&&!"".equals(temp)){
//						String[] split = temp.split(",");
//						System.out.println("INSERT INTO `wins-dsp-new`.`dsp_t_tencent_size` values("
//								+ "'"+split[0].trim()+"', '"+split[1].trim()+"', '"+split[2].trim()+"', '"+split[3].trim()+"', '"+split[4].trim()+"', '"+split[5].trim()+"',"
//										+ " '"+split[6].trim()+"', '"+split[7].trim()+"', '"+split[8].trim()+"', '"+split[9].trim()+"', '"+split[10].trim()+"', '"+split[11].trim()+"', '"+split[12].trim()+"', '2016-09-12 14:38:20');");
//					}
//			}
 
//		String a[] = {"",""};
//		String b = "21474836584";
//		System.out.println(Long.parseLong(b));
		
//		String aString=",dddd,";
//		if (aString.endsWith(",")) {
//			aString = aString+"";
//			String[] split = aString.split(",");
//			System.out.println(split.length);
//			
//			
//		}
//		BigDecimal decimal = new BigDecimal("");
//		System.out.println(decimal);
//		BigDecimal sum = new BigDecimal("1.222222222");
//
//		BigDecimal divide = sum.divide(new BigDecimal(1), 2, BigDecimal.ROUND_HALF_EVEN);
//		System.out.println(divide);
//        BigDecimal sum = new BigDecimal(0.011);
//        sum.divide(new BigDecimal(1), 2, BigDecimal.ROUND_HALF_EVEN);
//        System.out.println(sum);
//        String a = "d||d";
//        String[] split = a.split("\\|");
//        System.out.println(split[1].equals(""));
        String a = "1156110000";
        System.out.println(a.substring(4,10));
    }

    public static void test() throws IOException {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:00:00");

        Map<String,BigDecimal> result = new HashMap<>();

        //2016-10-17 15:00:00        :29128534.000000
        //2016-10-17 15:00:00---------16800.000000
       // 29145334
        //2016-10-17 16:00:00---------36647.000000
        //2016-10-17 17:00:00---------3000.000000
        //
        String file = "C:\\Users\\wuchengbin\\Desktop\\新建文件夹\\184423";
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(file)));
        String temp;
        int count=0;
        while ((temp = bufferedReader.readLine()) != null) {
            if (!temp.contains("e1d69411-a973-483c-8569-11daa8f7e962")){
                continue;
            }
            String[] bidInfos = temp.split("\\|");
            //数据最后面增加了ip字段
            if (bidInfos.length == 7 || bidInfos.length == 8) {
                BigDecimal price = new BigDecimal(bidInfos[5]);
                String format = sdf.format(new Date(Long.parseLong(bidInfos[0])));
                BigDecimal bigDecimal = result.get(format);
                if(bigDecimal==null){
                    result.put(format,price);
                }else {
                    result.put(format,bigDecimal.add(price));
                }
                count++;
            }
        }
        for (String s : result.keySet()) {
            System.out.println(s+"---------"+result.get(s));
        }

    }
}
