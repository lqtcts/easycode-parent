package com.logread.util;

import com.redistest.JedisUtil;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * -------------------------------------------------------------------------
 * 版权所有：上海蓬景数字营销策划有限公司
 * 作者：wuchengbin
 * 联系方式：wuchengbin@pxene.com.cn
 * 创建时间：2016年12月01日  15:18
 * 版本号：v1.0
 * 本类主要用途描述：
 * -------------------------------------------------------------------------
 */
public class MccUtil {
    public static final BigDecimal ZERO = new BigDecimal(0);
    public static final BigDecimal ONE = new BigDecimal(1);
    public static final BigDecimal HUNDRED = new BigDecimal(100);
    public static final BigDecimal THOUSAND = new BigDecimal(1000);

    //数据；类型
    public static int DATATYPE = 0;
    //数据维度
    public static int DIMENSION = 0;
    //时间格式化
    public static SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    public static SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");

    //counter类型的数据验证
    public static final String COUNTER_TEG = "mtype";
    public static final String COUNTER_COST = "price";
    public static final int IMCLI = 1;
    public static final int COST = 2;

    public static String startDate = "";
    public static String endDate = "";
    public static String path = "/data1/log/dspads_odin";
    public static final String pathCounterSub = "dsp_impclk";
    public static final String pathSettleSub = "dsp_settle";

    /**
     * 维度数据类型
     */
    public static final int DEVICE = 1;//操作系统|设备类型
    public static final int NETWORK = 2;//网络|运营商
    public static final int CREATIVE = 3;
    public static final int APP = 4;
    public static final int IP = 5;//地域
    public static final int PHONEBRAND = 6;//品牌|手机型号


    /**
     * 解析log用-匹配
     */
    public static final String ADX = "adx";//adx
    public static final String MAPID = "mapid";//adx
    public static final String MTYPE = "mtype";//类型 m\c
    public static final String APPID = "appid";//appid
    public static final String NETWORK_LOG = "network";//网络
    public static final String OS = "os";//操作系统
    public static final String REQIP = "reqip";//请求ip
    public static final String DEVICETYPE = "devicetype";//设备类型
    public static final String REQLOC = "reqloc";//请求地域码
    public static final String BRAND = "brand";//手机品牌
    public static final String CARRIER = "carrier";//运营商
    public static final String MODEL = "model";//手机型号
    public static final String PRICE = "price";//花费


    public static final String COST_STR = "cost";
    public static final String IMPL_STR = "impl";
    public static final String CLIC_STR = "clic";


    /**
     * 创意展现形式map
     */
    public static ConcurrentHashMap<String, String> impType = new ConcurrentHashMap<>();

    /**
     * app分类
     */
    public static ConcurrentHashMap<String, String> appc = new ConcurrentHashMap<>();

    /**
     * 时间格式转化，输入yyyyMMdd  输出yyyy-MM-dd
     *
     * @param date yyyyMMdd
     * @return
     */
    public static synchronized String dateFormat(String date) {
        try {
            return format2.format(format.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 时间格式转化，输入yyyyMMdd  输出yyyy-MM-dd
     *
     * @param date yyyyMMdd
     * @return
     */
    public static synchronized String dateFormat(Date date) {
        try {
            return format2.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 验证是否需要处理counter
     *
     * @return
     */
    public static synchronized boolean getCounterFlag(String data) {
        //如果是counter
        if (data.contains(COUNTER_TEG)) {
            if (DATATYPE == 0 || DATATYPE == IMCLI) {
                return true;
            }
        }
        return false;
    }

    public static synchronized boolean getSettleFlag(String data) {
        //如果是counter
        if (!data.contains(COUNTER_TEG)) {
            if (DATATYPE == 0 || DATATYPE == COST) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证是否需要处理这个维度
     *
     * @param dimenson
     * @return
     */
    public static synchronized boolean dimensonFlag(String dimenson) {

        if (DIMENSION == 0) {
            return true;
        } else {
            switch (dimenson) {
                case APPID://app
                    if (DIMENSION == APP) {
                        return true;
                    } else {
                        return false;
                    }
                case DEVICETYPE://操作系统|设备类型
                    if (DIMENSION == DEVICE) {
                        return true;
                    } else {
                        return false;
                    }
                case REQLOC://地域
                    if (DIMENSION == IP) {
                        return true;
                    } else {
                        return false;
                    }
                case CARRIER://网络|运营商
                    if (DIMENSION == NETWORK) {
                        return true;
                    } else {
                        return false;
                    }
                case MODEL://品牌|手机型号
                    if (DIMENSION == PHONEBRAND) {
                        return true;
                    } else {
                        return false;
                    }
                case MAPID://创意
                    if (DIMENSION == CREATIVE) {
                        return true;
                    } else {
                        return false;
                    }
                default:
                    return false;
            }
        }
    }

    /**
     * 花费
     */
    public static final String DATATYPE_S = "s";

    /**
     * 切割符
     */
    public static final String SPLIT_BASE = "\\|";

    private static Map<String, BigDecimal> adxPriceCofiMappingMap = new HashMap<String, BigDecimal>();

    /**
     * 转换adx的花费单位
     *
     * @param category
     * @param sum
     * @return
     */
    public static BigDecimal getPriceByAdx(String category, BigDecimal sum) {
        BigDecimal ratio = adxPriceCofiMappingMap.get(category);
        if (ratio == null) {
            String ratioStr = JedisUtil.getStr("dsp_ratio_" + category);
            ratio = (ratioStr == null ? MccUtil.ONE : new BigDecimal(ratioStr));
            adxPriceCofiMappingMap.put(category, ratio);
        }

        if (category.equals("2") || category.equals("22")) {//除以10000等于元
            sum = sum.divide(MccUtil.HUNDRED);
        } else if (category.equals("3")) {//结算单位为元，需将平台系数修改为1
            sum = sum.multiply(MccUtil.HUNDRED);
        } else if (category.equals("14")) {//除以10000等于元
            sum = sum.divide(MccUtil.HUNDRED);
        } else if (category.equals("10")) {//除以10000等于元
            sum = sum.divide(MccUtil.HUNDRED);
        } else if (category.equals("13")) {//结算单位为元
            sum = sum.multiply(MccUtil.HUNDRED);
        } else if (category.equals("16")) {//折算成千次展现分
            sum = sum.divide(BigDecimal.TEN);
        }
        //平台系数
        sum = sum.divide(ratio, 2, BigDecimal.ROUND_HALF_EVEN);
        return sum;
    }
}
