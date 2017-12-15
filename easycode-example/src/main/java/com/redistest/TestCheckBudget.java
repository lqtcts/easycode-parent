package com.redistest;

import java.io.IOException;
import java.math.BigDecimal;

public class TestCheckBudget {
	public static void main(String[] args) throws IOException {
		String[] groupidStrings = { 
//				"a9b3a62b-262a-4908-bcc0-5e9cfdaefdfa", 
//				"e677a122-141d-403c-9290-cdcbd2c0ff41", 
				"6e3ec217-370a-49ea-b670-b508fee69413",
				"9ed9f05a-cdcf-4c12-827e-6d503f43166d",
				"d125f86d-aff2-448a-8233-a53226d9f0ac", 
				"29518561-034f-465f-aec4-6fb1fb21b8c0", 
				"cea673f6-fd39-49c5-a7d1-e2d0bcda0d78" };
		for (int i = 0; i < groupidStrings.length; i++) {
			showSom(groupidStrings[i]);
			System.out.println("-------------------");
			
		}

	}

	public static void showSom(String groupid) {
		String[] keys = JedisUtil.getKeys("dsp_budget_*_" + groupid);
		BigDecimal totals = new BigDecimal(0);
		for (String key : keys) {
			System.out.println(key + "-----------:" + JedisUtil.getStr(key));
			totals = totals.add(new BigDecimal(JedisUtil.getStr(key)));
		}
		System.out.println("budget_balance_" + groupid + "-----------:余额==》" + JedisUtil.getStr("budget_balance_" + groupid));
		totals = totals.add(new BigDecimal(JedisUtil.getStr("budget_balance_" + groupid)));
		System.out.println(totals.toPlainString());

		keys = JedisUtil.getKeys("dsp_counter_*_" + groupid);
		for (String key : keys) {
			System.out.println(key + "-----------:" + JedisUtil.getStr(key));
		}
		System.out.println("counter_balance_" + groupid + "-----------:余额==》" + JedisUtil.getStr("counter_balance_" + groupid));
	}

}
