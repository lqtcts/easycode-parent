package com.logread.util;

import com.utils.*;
import com.utils.DateUtil;

import java.io.File;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.l;
import static org.yecht.LevelStatus.map;

/**
 * -------------------------------------------------------------------------
 * 版权所有：上海蓬景数字营销策划有限公司
 * 作者：wuchengbin
 * 联系方式：wuchengbin@pxene.com.cn
 * 创建时间：2016年12月08日  16:51
 * 版本号：v1.0
 * 本类主要用途描述：
 * -------------------------------------------------------------------------
 */
public class SumAll {
    /**
     * 汇总数据
     */
    public Map<Integer,ConcurrentHashMap<String,String>> map = new ConcurrentHashMap();


    /**
     * 汇总信息
     * @param map
     */
    public synchronized  static void addAll(Map<String,Object> map){

    }

    public static void main(String[] args) {
//        File dic = new File("C:\\Users\\wuchengbin.POWERXENE\\Desktop\\dspads_odin\\dsp_settle\\dsp_settle_20161208");
//        File[] files = dic.listFiles();
//        for(File file : files){
//            System.out.println(file.getPath());
//        }
        String a = "ddd,,,,,111";
        String[] split = a.split(",");
        System.out.println(split.length);
    }

}
