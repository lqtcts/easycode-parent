package com.logread.datasql;

import com.logread.util.MccUtil;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.logread.util.MccUtil.appc;
import static com.logread.util.MccUtil.impType;
import static org.yecht.LevelStatus.str;
import static sun.audio.AudioDevice.device;

/**
 */
public class CounterSettleDataSqlEx extends DataSqlExcute {


    public void insertOrUpdateDataMcc(Map<String, ConcurrentHashMap<String, Object>> sumMap) {
        // 86ca04ee-880a-4df2-bc02-cc59cb7c6116,2016-12-08,2,1|0---{clic=53154, impl=53154, cost=53154000.00}
        //遍历写入不同维度
        for (String s : sumMap.keySet()) {
            String[] split = s.split(",");
            int dataType = Integer.parseInt(split[2]);
            //写入不同维度
            if (MccUtil.DEVICE == dataType) {
                insertOrUpdateDataMccDevice(s, sumMap.get(s));
            } else if (MccUtil.NETWORK == dataType) {
                insertOrUpdateDataMccNetwork(s, sumMap.get(s));
            } else if (MccUtil.CREATIVE == dataType) {
                insertOrUpdateDataMccCreative(s, sumMap.get(s));
            } else if (MccUtil.APP == dataType) {
                insertOrUpdateDataMccAppAndAppCategory(s, sumMap.get(s));
            } else if (MccUtil.IP == dataType) {
                insertOrUpdateDataMccIp(s, sumMap.get(s));
            } else if (MccUtil.PHONEBRAND == dataType) {
                insertOrUpdateDataMccPhoneBrand(s, sumMap.get(s));
            } else {
                System.out.println("data mcc insert error ! not matching   dataType  , the dataType is :" + dataType);
            }
        }
    }

    /**
     * 入库设备平台操作系统点击展现花费统计表
     * 操作系统|设备类型
     */
    public synchronized  void insertOrUpdateDataMccDevice(String key, ConcurrentHashMap<String, Object> sumMap) {
        //0e8e6faa-dbcd-4f44-9f1e-2c49183c6046,2016-12-08,1,1|2---{clic=3464, impl=3464, cost=3660000.00}
        String[] split = key.split(",");
        String mapid = split[0];
        String time = split[1];
//        String dataType = split[2];
        String dataValueS = split[3];
        String[] dataValue = dataValueS.split("\\|");

        //
        String os = dataValue[0];
        String device = dataValue[1];
        String sql = " select  * from dsp_t_mcc_device_count  ";
        String where = "where mapid='" + mapid + "' and time ='" + time + "' and devicecode = '" + device + "' and systemcode ='" + os + "'";
        //查看记录是否存在
        if (!getDataExist(sql + where)) {
            String insertSql = "INSERT INTO  dsp_t_mcc_device_count VALUES " +
                    "('" + mapid + "', '" + time + "', '" + device + "', '" + os + "'" + getImplClicCostInsertOrUpdateValue(sumMap, true) + ")";
            inserToMysql(insertSql);
        } else {
            String updateSql = "update dsp_t_mcc_device_count " + getImplClicCostInsertOrUpdateValue(sumMap, false) + where;
            updateToMysql(updateSql);
        }
    }


    /**
     * 入库网络点击展现花费统计表
     * 网络|运营商
     */
    public synchronized void insertOrUpdateDataMccNetwork(String key, ConcurrentHashMap<String, Object> sumMap) {
        //0e8e6faa-dbcd-4f44-9f1e-2c49183c6046,2016-12-08,1,1|2---{clic=3464, impl=3464, cost=3660000.00}
        String[] split = key.split(",");
        String mapid = split[0];
        String time = split[1];
//        String dataType = split[2];
        String dataValueS = split[3];
        String[] dataValue = dataValueS.split("\\|");
        //
        String network = dataValue[0];
        String provider = dataValue[1];
        String sql = " select  * from dsp_t_mcc_network_count  ";
        String where = "where mapid='" + mapid + "' and time ='" + time + "' and providercode = '" + provider + "' and networkcode ='" + network + "'";
        //查看记录是否存在
        if (!getDataExist(sql + where)) {
            String insertSql = "INSERT INTO  dsp_t_mcc_network_count VALUES " +
                    "('" + mapid + "', '" + time + "', '" + network + "', '" + provider + "'" + getImplClicCostInsertOrUpdateValue(sumMap, true) + ")";
            inserToMysql(insertSql);
        } else {
            String updateSql = "update dsp_t_mcc_network_count " + getImplClicCostInsertOrUpdateValue(sumMap, false) + where;
            updateToMysql(updateSql);
        }
    }

    /**
     * 入库设展现形式点击展现花费统计表
     */
    public synchronized void insertOrUpdateDataMccCreative(String key, ConcurrentHashMap<String, Object> sumMap) {
        String[] split = key.split(",");
        String mapid = split[0];
        String time = split[1];
//        String dataValueS = split[3];
        String imptype = getImpTypeByMapid(mapid);
        String sql = " select  * from dsp_t_mcc_creative_count  ";
        String where = "where mapid='" + mapid + "' and time ='" + time + "' and imptype = '" + imptype + "'";
        //查看记录是否存在
        if (!getDataExist(sql + where)) {
            String insertSql = "INSERT INTO  dsp_t_mcc_creative_count VALUES " +
                    "('" + mapid + "', '" + time + "',  '" + imptype + "'" + getImplClicCostInsertOrUpdateValue(sumMap, true) + ")";
            inserToMysql(insertSql);
        } else {
            String updateSql = "update dsp_t_mcc_creative_count " + getImplClicCostInsertOrUpdateValue(sumMap, false) + where;
            updateToMysql(updateSql);
        }
    }

    /**
     * 入库app和分类点击展现花费统计表   ，在前台查询app的时候 自然会关联app码表，该码表中有app分类code，
     */
    public synchronized void insertOrUpdateDataMccAppAndAppCategory(String key, ConcurrentHashMap<String, Object> sumMap) {
        String[] split = key.split(",");
        String mapid = split[0];
        String time = split[1];
//        String dataType = split[2];
        String dataValueS = split[3];
        String[] dataValue = dataValueS.split("\\|");
        String adx = getMCCDataNull(dataValue[0]);
        String appid = null;//解码
        try {
            appid = URLDecoder.decode(getMCCDataNull(dataValue[1]), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //
        String sql = " select  * from dsp_t_mcc_app_count  ";
        String where = "where mapid='" + mapid + "' and time ='" + time + "' and appid = '" + appid + "' and adxvalue ='" + adx + "'";
        //查看记录是否存在
        if (!getDataExist(sql + where)) {
            String insertSql = "INSERT INTO  dsp_t_mcc_app_count VALUES " +
                    "('" + mapid + "', '" + time + "', '" + appid + "', '" + adx + "'" + getImplClicCostInsertOrUpdateValue(sumMap, true) + ")";
            inserToMysql(insertSql);
        } else {
            String updateSql = "update dsp_t_mcc_app_count " + getImplClicCostInsertOrUpdateValue(sumMap, false) + where;
            updateToMysql(updateSql);
        }

        //app分类处理
        String category = getCategoryCodeByAppidAndAdx(appid);
        String sqlCategory = " select  * from dsp_t_mcc_app_category_count  ";
        String whereCategory = "where mapid='" + mapid + "' and time ='" + time + "' and categorycode = '" + category + "'";
        //查看记录是否存在
        if (!getDataExist(sqlCategory + whereCategory)) {
            String insertSql = "INSERT INTO  dsp_t_mcc_app_category_count VALUES " +
                    "('" + mapid + "', '" + time + "',  '" + category + "'" + getImplClicCostInsertOrUpdateValue(sumMap, true) + ")";
            inserToMysql(insertSql);
        } else {
            String updateSql = "update dsp_t_mcc_app_category_count " + getImplClicCostInsertOrUpdateValue(sumMap, false) + whereCategory;
            updateToMysql(updateSql);
        }
    }

    /**
     * 入库地域点击展现花费统计表
     */
    public synchronized void insertOrUpdateDataMccIp(String key, ConcurrentHashMap<String, Object> sumMap) {
        //0e8e6faa-dbcd-4f44-9f1e-2c49183c6046,2016-12-08,1,1|2---{clic=3464, impl=3464, cost=3660000.00}
        String[] split = key.split(",");
        String mapid = split[0];
        String time = split[1];
        String dataValueS = split[3];
        String city = dataValueS.substring(4, 10);
        String prov = city.substring(0, 2) + "0000";
        String sql = " select  * from dsp_t_mcc_ip_count  ";
        String where = "where mapid='" + mapid + "' and time ='" + time + "' and provincecode = '" + prov + "' and citycode ='" + city + "'";
        //查看记录是否存在
        if (!getDataExist(sql + where)) {
            String insertSql = "INSERT INTO  dsp_t_mcc_ip_count VALUES " +
                    "('" + mapid + "', '" + time + "', '" + prov + "', '" + city + "'" + getImplClicCostInsertOrUpdateValue(sumMap, true) + ")";
            inserToMysql(insertSql);
        } else {
            String updateSql = "update dsp_t_mcc_ip_count " + getImplClicCostInsertOrUpdateValue(sumMap, false) + where;
            updateToMysql(updateSql);
        }
    }

    /**
     * 入库手机品牌点击展现花费统计表
     */
    public synchronized void insertOrUpdateDataMccPhoneBrand(String key, ConcurrentHashMap<String, Object> sumMap) {
        //0e8e6faa-dbcd-4f44-9f1e-2c49183c6046,2016-12-08,1,1|2---{clic=3464, impl=3464, cost=3660000.00}
        if (key.endsWith("|")) {
            key = key + "NULL";
        }

        String[] split = key.split(",");
        String mapid = split[0];
        String time = split[1];
        String dataValueS = split[3];
        String[] dataValue = dataValueS.split("\\|");
        String brand = dataValue[0];
        String model = dataValue[1];
        //解码手机型号
        if (model != null && !model.equals("NULL")) {
            try {
                model = URLDecoder.decode(model, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        String sql = " select  * from dsp_t_mcc_phonebrand_count  ";
        String where = "where mapid='" + mapid + "' and time ='" + time + "' and phonebrandid = '" + brand + "' and model ='" + model + "'";
        //查看记录是否存在
        if (!getDataExist(sql + where)) {
            String insertSql = "INSERT INTO  dsp_t_mcc_phonebrand_count VALUES " +
                    "('" + mapid + "', '" + time + "', '" + brand + "', '" + model + "'" + getImplClicCostInsertOrUpdateValue(sumMap, true) + ")";
            inserToMysql(insertSql);
        } else {
            String updateSql = "update dsp_t_mcc_phonebrand_count " + getImplClicCostInsertOrUpdateValue(sumMap, false) + where;
            updateToMysql(updateSql);
        }
    }

    /**
     * 处理新增sql中的impl、clic、cost
     *
     * @param sumMap
     * @return
     */
    private String getImplClicCostInsertOrUpdateValue(ConcurrentHashMap<String, Object> sumMap, boolean insertFlag) {
        Integer clic = sumMap.get(MccUtil.CLIC_STR) == null ? null : (Integer) sumMap.get(MccUtil.CLIC_STR);
        Integer impl = sumMap.get(MccUtil.IMPL_STR) == null ? null : (Integer) sumMap.get(MccUtil.IMPL_STR);
        BigDecimal cost = sumMap.get(MccUtil.COST_STR) == null ? null : (BigDecimal) sumMap.get(MccUtil.COST_STR);
        if (insertFlag) {
            return ", '" + (clic == null ? 0 : clic) + "', '" + (impl == null ? 0 : impl) + "', '" + (cost == null ? 0 : cost) + "', now()";
        } else {
            String implClicCostSet = "";
            if(impl!=null){
                implClicCostSet = implClicCostSet+", clicks=" + clic;
            }
            if(clic!=null){
                implClicCostSet = implClicCostSet+", impression=" + impl;
            }
            if(cost!=null){
                implClicCostSet = implClicCostSet+", cost=" + cost;
            }
            return " set "+ implClicCostSet.substring(1)+" ";
        }
    }


    public String getMCCDataNull(String s) {
        if (s == null || "".equals(s)) {
            return "NULL";
        } else {
            return s;
        }
    }

    /**
     * 查询app分类
     *
     * @param appid
     * @return
     */
    private String getCategoryCodeByAppidAndAdx(String appid) {
        String sql = " SELECT IFNULL( ( SELECT firstcode FROM dsp_t_syn_app_adx  WHERE  appid = '" + appid + "' LIMIT 1 ), 'NULL' )";

        String category = MccUtil.appc.get(appid);
        if (category == null || "".equals(category)) {
            String categoryCodeByAppidAndAdx = selectOnlyValue(sql);
            if (null == categoryCodeByAppidAndAdx || "".equals(categoryCodeByAppidAndAdx)) {
                categoryCodeByAppidAndAdx = "NULL";
            }
            appc.put(appid, categoryCodeByAppidAndAdx);
            return categoryCodeByAppidAndAdx;
        } else {
            return category;
        }
    }

    /**
     * 获取创意的展现形式
     * mapid
     * @return 00 标识未知
     */
    private synchronized String getImpTypeByMapid(String mapid) {
        String sql = " SELECT\n" +
                "            IFNULL(max(t5.value),'0')\n" +
                "        FROM\n" +
                "            dsp_t_ad_group_creative t2\n" +
                "        LEFT JOIN dsp_t_ad_creative t ON t.id = t2.creativeid\n" +
                "        LEFT JOIN dsp_t_source t3 ON t.materialid = t3.id\n" +
                "        LEFT JOIN dsp_t_ad_size t4 ON t4.id = t3.sizeid\n" +
                "\t\t\t\tLEFT JOIN dsp_t_ad_position_type t5 on t4.promotion = t5.promotion\n" +
                "        where t2.id='" + mapid + "'\n" +
                "        limit 1";

        //map里面有直接取，没有查询数据库
        String promotion = MccUtil.impType.get(mapid);
        if (promotion == null || "".equals(promotion)) {

            String promInTable = selectOnlyValue(sql);
            if (null == promInTable || "".equals(promInTable)) {
                promInTable = "0";
            }
            impType.put(mapid, promInTable);
            return promInTable;
        } else {
            return promotion;
        }
    }



    public static void main(String[] args) {
//        boolean dataExist = new CounterSettleDataSqlEx().getDataExist(" select  * from dsp_t_mcc_device_count  where mapid='000d46e3-3564-40d1-bb34-13ae1d3d0296s'  ");
//        System.out.println(dataExist);
        Map<String, ConcurrentHashMap<String, Object>> ddd = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, Object> value = new ConcurrentHashMap<>();
        value.put(MccUtil.IMPL_STR, 111);
        value.put(MccUtil.CLIC_STR, 22);
        value.put(MccUtil.COST_STR, new BigDecimal(1.1));
        ddd.put("ddddddddddd,2016-12-08,1,1|2", value);
        new CounterSettleDataSqlEx().insertOrUpdateDataMcc(ddd);
    }
}
