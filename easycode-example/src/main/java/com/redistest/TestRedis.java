package com.redistest;


import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

public class TestRedis {
	
	
	public static void main(String[] args) throws IOException {
		String baimdString="dsp_groupid_wblist_4c31425a-2b66-4452-9586-c55264be4b29";//白名单
		
		
//		String groupid="28b473a5-7a7f-4ecf-86bb-74a043926ec4";   balance_bbbaa199-cc86-4a31-ae85-62a41f85f7d8-----------:20000
		String groupid="054b1902-e58b-4a1f-ba7e-c2f4ccfe520b";
//		String groupid="a9087e33-6533-4c6b-8fbb-0220c6832a0c";
//		String groupid="90e07b7d-44f6-4495-a920-afcc2ada18ad";
		
//		String accountId="bbbaa199-cc86-4a31-ae85-62a41f85f7d8";
//		showBalance(accountId);
//		showSom(groupid);
//		showOut(groupid) ;
//		showWB(groupid);
//		delWB(groupid);
		showLike(groupid);
	}

	
	public static void showLike(String groupid){
		String[] keys = JedisUtil.getKeys("*"+groupid);
		Arrays.sort(keys);
		for (String string : keys) {
			String obj = JedisUtil.getStr(string);
			System.out.println(string+"    :"+obj);
		}
	}
	 
	
	public static void showSom(String groupid){
		
		System.out.println("--------------------------预算 kpi  信息  推广组-----------------------------");
		
		String[] keyStart={
				"dsp_budget_1_",
				"dsp_budget_2_",
				"dsp_budget_3_",
				"budget_balance_",
				"dsp_counter_1_",
				"dsp_counter_2_",
				"dsp_counter_3_",
				"counter_balance_",
				"dsp_groupid_frequencycapping_"	
		};
		for (int i = 0; i < keyStart.length; i++) {
			System.out.println(keyStart[i]+groupid+"-----------:"+JedisUtil.getStr(keyStart[i]+groupid));
		}
		
	}
	
	public static void showOut(String groupid){
		System.out.println("--------------------------超出预算 kpi ------------------------------");
		
		
		
		String[] keyStart={
				"out_of_budget_campaigns",
				"out_of_counter_groups",
				"out_of_budget_groups",
		};
		for (int i = 0; i < keyStart.length; i++) {
			String str = JedisUtil.getStr(keyStart[i]);
			if (str!=null&&!"".equals(str)) {
				if (str.contains(groupid)) {
					System.out.println(keyStart[i]+"--contains--"+groupid+"   result:"+true);
				}else {
					System.out.println(keyStart[i]+"--contains--"+groupid+"   result:"+false);
				}
			}
		}
		
	}
	
	public static void showBalance(String accountId){
			System.out.println("balance_"+accountId+"-----------:"+JedisUtil.getStr("balance_"+accountId));
	}
	
	public static void delSom(String groupid){
		System.out.println("--------------------------删除预算kpi  推广组 ------------------------------");
	
		String[] keyStart={
				"dsp_budget_1_",
				"dsp_budget_2_",
				"dsp_budget_3_",
				"budget_balance_",
				"dsp_counter_1_",
				"dsp_counter_2_",
				"dsp_counter_3_",
				"counter_balance_",
		};
		for (int i = 0; i < keyStart.length; i++) {
			 JedisUtil.delete(keyStart[i]+groupid);
			 System.out.println("delete key success:"+keyStart[i]+groupid);
		}
	}
	
	String aString="dsp_groupid_frequencycapping_";
	
	public static void showWB(String groupid){
		
		System.out.println("--------------------------黑白名单  推广组 ------------------------------");
 
		String[] keyStart={
				"mac_wl_",
				"mac_sha1_",
				"mac_md5_wl_",
				"mac_sha1_wl_",
				"imei_wl_",
				"imei_sha1_",
				"imei_md5_wl_",
				"imei_sha1_wl_",
				"idfa_wl_",
				"idfa_sha1_",
				"idfa_md5_wl_",
				"idfa_sha1_wl_",
				"android_wl_",
				"android_sha1_",
				"android_md5_wl_",
				"android_sha1_wl_",
				"dsp_groupid_wblist_",
		};
		for (int i = 1; i <= keyStart.length; i++) {
			System.out.println(keyStart[i-1]+groupid+"-----------:"+JedisUtil.sget(keyStart[i-1]+groupid));
			if (i!=0&&i%4 ==0) {
				System.out.println("--------------------------------------------------------------------------------------");
			}
		}
	}
	public static void delWB(String groupid){
		
		System.out.println("--------------------------黑白名单  推广组 ------------------------------");
		
		String[] keyStart={
				"mac_wl_",
				"mac_sha1_",
				"mac_md5_wl_",
				"mac_sha1_wl_",
				"imei_wl_",
				"imei_sha1_",
				"imei_md5_wl_",
				"imei_sha1_wl_",
				"idfa_wl_",
				"idfa_sha1_",
				"idfa_md5_wl_",
				"idfa_sha1_wl_",
				"android_wl_",
				"android_sha1_",
				"android_md5_wl_",
				"android_sha1_wl_",
				"dsp_groupid_wblist_",
		};
		for (int i = 1; i <= keyStart.length; i++) {
			String aaString=keyStart[i-1]+groupid;
			JedisUtil.deleteByPattern(aaString);
			System.out.println(aaString+"-----------:delete");
		}
	}
	
}
