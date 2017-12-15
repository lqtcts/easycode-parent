package com.logread.handle;

import com.logread.util.MccUtil;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.c;

/**
 * -------------------------------------------------------------------------
 * 版权所有：上海蓬景数字营销策划有限公司
 * 作者：wuchengbin
 * 联系方式：wuchengbin@pxene.com.cn
 * 创建时间：2016年12月07日  15:00
 * 版本号：v1.0
 * 本类主要用途描述：
 * -------------------------------------------------------------------------
 */
public class SettleHandle implements IHandle {
    private Map<String, BigDecimal> costMap = new ConcurrentHashMap<>();

    @Override
    public void handle(String line) {
        //是否有花费数据
        BigDecimal price = null;
        boolean containsPriceFlag = line.contains(MccUtil.COUNTER_COST);

        //切割遍历数据
        String[] motionInfos = line.split(",");
        if (motionInfos.length<19){
            return ;
        }
        String time = MccUtil.dateFormat(motionInfos[0]);
        String data = null;
        String mapid = null;
        String adx = null;
        String os = null;
        String network = null;
        String brand = null;
        //"ip:%s,adx:%d,bid:%s,mapid:%s,deviceid:%s,deviceidtype:%s,mtype:%c,appid:%s,network:%s,os:%s,devicetype:%s,reqip:%s,reqloc:%s,brand:%s,carrier:%s,model:%s,price:%f"
        //请求id，设备id，设备类型，创意id，展现或点击，平台，(曝光|点击|结算)IP，appid,网络，操作系统，设备类型,qqip,请求地域，品牌，运营商,手机型号
        for (int i = 0; i < motionInfos.length; i++) {

            String value = motionInfos[i];

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
                data = mapid + "," + time + "," + MccUtil.APP + "," + adx + "|" + value.split(":")[1];
                this.setSettleMapData(data, price);
                continue;
            }
            //操作系统|设备类型
            if (value.contains(MccUtil.OS)) {//操作系统
                os = value.split(":")[1];
                continue;
            }
            if (value.contains(MccUtil.DEVICETYPE) && MccUtil.dimensonFlag(MccUtil.DEVICETYPE)) {//操作系统|设备类型
                data = mapid + "," + time + "," + MccUtil.DEVICE + "," + os + "|" + value.split(":")[1];
                this.setSettleMapData(data, price);
                continue;
            }

            //地域
            if (value.contains(MccUtil.REQLOC) && MccUtil.dimensonFlag(MccUtil.REQLOC)) {//地域
                data = mapid + "," + time + "," + MccUtil.IP + "," + value.split(":")[1];
                this.setSettleMapData(data, price);
                continue;
            }

            //网络|运营商
            if (value.contains(MccUtil.NETWORK_LOG)) {//网络
                network = value.split(":")[1];
                continue;
            }
            if (value.contains(MccUtil.CARRIER) && MccUtil.dimensonFlag(MccUtil.CARRIER)) {//网络|运营商
                data = mapid + "," + time + "," + MccUtil.NETWORK + "," + network + "|" + value.split(":")[1];
                this.setSettleMapData(data, price);
                continue;
            }

            //品牌|手机型号
            if (value.contains(MccUtil.BRAND)) {//品牌
                brand = value.split(":")[1];
                continue;
            }
            if (value.contains(MccUtil.MODEL) && MccUtil.dimensonFlag(MccUtil.MODEL)) {//品牌|手机型号
                data = mapid + "," + time + "," + MccUtil.PHONEBRAND + "," + brand + "|" + value.split(":")[1];
                this.setSettleMapData(data, price);
                continue;
            }
        }

        //展现形式
        if (MccUtil.dimensonFlag(MccUtil.MAPID)) {
            data = mapid + "," + time + "," + MccUtil.CREATIVE + "," + mapid;
            this.setSettleMapData(data, price);
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

    @Override
    public Map<String, Integer> getMap() {
        return null;
    }

    @Override
    public Map<String, BigDecimal> getCostMap() {
        return costMap;
    }
}
