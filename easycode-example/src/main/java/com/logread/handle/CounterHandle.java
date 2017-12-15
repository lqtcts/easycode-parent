package com.logread.handle;

import backtype.storm.tuple.Values;
import com.logread.util.MccUtil;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * -------------------------------------------------------------------------
 * 版权所有：上海蓬景数字营销策划有限公司
 * 作者：wuchengbin
 * 联系方式：wuchengbin@pxene.com.cn
 * 创建时间：2016年12月07日  14:59
 * 版本号：v1.0
 * 本类主要用途描述：
 * -------------------------------------------------------------------------
 */
public class CounterHandle implements IHandle {
    private Map<String, Integer> map = new ConcurrentHashMap<>();
    private Map<String, BigDecimal> costMap = new ConcurrentHashMap<>();

    public CounterHandle() {
    }

    public static void main(String[] args) {
    }

    @Override
    public void handle(String line) {
        //是否有花费数据
        BigDecimal price = null;
        boolean containsPriceFlag = line.contains(MccUtil.COUNTER_COST);

        //切割遍历数据
        String[] motionInfos = line.split(",");
        if (motionInfos.length < 19) {
            return;
        }
        String time = MccUtil.dateFormat(motionInfos[0]);
        String data = null;
        String mapid = null;
        String adx = null;
        String mType = null;
        String os = null;
        String network = null;
        String brand = null;

        //"ip:%s,adx:%d,bid:%s,mapid:%s,deviceid:%s,deviceidtype:%s,mtype:%c,appid:%s,network:%s,os:%s,devicetype:%s,reqip:%s,reqloc:%s,brand:%s,carrier:%s,model:%s,price:%f"
        //请求id，设备id，设备类型，创意id，展现或点击，平台，(曝光|点击|结算)IP，appid,网络，操作系统，设备类型,qqip,请求地域，品牌，运营商,手机型号
        for (int i = 0; i < motionInfos.length; i++) {

            String value = motionInfos[i];

            if (value.contains(MccUtil.MTYPE)) {
                mType = value.split(":")[1];
                continue;
            }
            if (value.contains(MccUtil.ADX)) {
                adx = value.split(":")[1];
                if (containsPriceFlag) {
                    String substring = line.substring(line.indexOf(MccUtil.COUNTER_COST), line.length());
                    price = MccUtil.getPriceByAdx(adx, new BigDecimal(substring.split(":")[1]));
                }
                continue;
            }
            if (value.contains(MccUtil.MAPID)) {
                mapid = value.split(":")[1];
                continue;
            }

            //app
            if (value.contains(MccUtil.APPID) && MccUtil.dimensonFlag(MccUtil.APPID)) {//app 需要adx|appid
                data = mapid + "," + mType + "," + time + "," + MccUtil.APP + "," + adx + "|" + value.split(":")[1];
                this.setCounterMapData(data);
                //有花费处理花费
                if (containsPriceFlag) {
                    data = mapid + "," + time + "," + MccUtil.APP + "," + adx + "|" + value.split(":")[1];
                    setSettleMapData(data, price);
                }
                continue;
            }

            //操作系统|设备类型
            if (value.contains(MccUtil.OS)) {//操作系统
                os = value.split(":")[1];
                continue;
            }
            if (value.contains(MccUtil.DEVICETYPE) && MccUtil.dimensonFlag(MccUtil.DEVICETYPE)) {//操作系统|设备类型
                data = mapid + "," + mType + "," + time + "," + MccUtil.DEVICE + "," + os + "|" + value.split(":")[1];
                this.setCounterMapData(data);
                //有花费处理花费
                if (containsPriceFlag) {
                    data = mapid + "," + time + "," + MccUtil.DEVICE + "," + os + "|" + value.split(":")[1];
                    setSettleMapData(data, price);
                }
                continue;
            }


            //地域
            if (value.contains(MccUtil.REQLOC) && MccUtil.dimensonFlag(MccUtil.REQLOC)) {//地域
                data = mapid + "," + mType + "," + time + "," + MccUtil.IP + "," + value.split(":")[1];
                this.setCounterMapData(data);
                //有花费处理花费
                if (containsPriceFlag) {
                    data = mapid + "," + time + "," + MccUtil.IP + "," + value.split(":")[1];
                    setSettleMapData(data, price);
                }
                continue;
            }


            //网络|运营商
            if (value.contains(MccUtil.NETWORK_LOG)) {//网络
                network = value.split(":")[1];
                continue;
            }
            if (value.contains(MccUtil.CARRIER) && MccUtil.dimensonFlag(MccUtil.CARRIER)) {//网络|运营商
                data = mapid + "," + mType + "," + time + "," + MccUtil.NETWORK + "," + network + "|" + value.split(":")[1];
                this.setCounterMapData(data);
                //有花费处理花费
                if (containsPriceFlag) {
                    data = mapid + "," + time + "," + MccUtil.NETWORK + "," + network + "|" + value.split(":")[1];
                    setSettleMapData(data, price);
                }
                continue;
            }


            //品牌|手机型号
            if (value.contains(MccUtil.BRAND)) {//品牌
                brand = value.split(":")[1];
                continue;
            }
            if (value.contains(MccUtil.MODEL) && MccUtil.dimensonFlag(MccUtil.MODEL)) {//品牌|手机型号
                data = mapid + "," + mType + "," + time + "," + MccUtil.PHONEBRAND + "," + brand + "|" + value.split(":")[1];
                this.setCounterMapData(data);
                //有花费处理花费
                if (containsPriceFlag) {
                    data = mapid + "," + time + "," + MccUtil.PHONEBRAND + "," + brand + "|" + value.split(":")[1];
                    setSettleMapData(data, price);
                }
                continue;
            }
        }

        //展现形式
        if (MccUtil.dimensonFlag(MccUtil.MAPID)) {
            data = mapid + "," + mType + "," + time + "," + MccUtil.CREATIVE + "," + mapid;
            this.setCounterMapData(data);
            //有花费处理花费
            if (containsPriceFlag) {
                data = mapid + "," + time + "," + MccUtil.CREATIVE + "," + mapid;
                setSettleMapData(data, price);
            }
        }

    }
    /**
     * sum data
     *
     * @param line
     */
    private void setCounterMapData(String line) {
        synchronized (this) {
            if (line != null) {
//				logger.info("----ksj:"+string2);
                this.map.put(line, Integer.valueOf((this.map.get(line) == null ? 0 : (this.map.get(line)).intValue()) + 1));
            }
        }
    }

    /**
     * sum data
     *
     * @param line
     */
    private void setSettleMapData(String line, BigDecimal price) {
        synchronized (this) {
            if (line != null) {
                this.costMap.put(line, this.costMap.get(line) == null ? price : (this.costMap.get(line)).add(price));
            }
        }
    }

    //    /**
//     * sum data
//     *
    public Map<String, Integer> getMap() {
        return map;
    }

    @Override
    public Map<String, BigDecimal> getCostMap() {
        return costMap;
    }
}
