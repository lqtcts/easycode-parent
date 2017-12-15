package com.logread.datainwork;

import com.logread.util.MccUtil;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.logread.util.MccUtil.COST_STR;
import static com.logread.util.SumAll.addAll;
import static org.bouncycastle.asn1.x500.style.RFC4519Style.o;


public class DataSumInworkImpl implements DataInwork {

    /**
     * 汇总数据
     */
    public Map<String, ConcurrentHashMap<String, Object>> sumMap = new ConcurrentHashMap();


    /**
     * 汇总信息 counter
     *
     * @param map
     */
    @Override
    public synchronized void inworkCounter(Map<String, Integer> map) {
        for (String s : map.keySet()) {
            //count数据
            Integer count = map.get(s);
            ConcurrentHashMap<String, Object> oldmap =null;
            //展现
            if (s.contains(",m,")) {
                String s1 = s.replaceAll("m,", "");
                oldmap = sumMap.get(s1);
                if (oldmap != null) {
                    Integer impl = (Integer) oldmap.get(MccUtil.IMPL_STR);
                    //有这个记录但是没有这个展现
                    if (impl != null) {
                        sumMap.get(s1).put(MccUtil.IMPL_STR, count + impl);
                    } else {
                        sumMap.get(s1).put(MccUtil.IMPL_STR, count);
                    }

                } else {
                    oldmap = new ConcurrentHashMap<>();
                    oldmap.put(MccUtil.IMPL_STR, count);
                    sumMap.put(s1, oldmap);
                }
            }
            //点击
            if (s.contains(",c,")) {
                String s1 = s.replaceAll("c,", "");
                oldmap = sumMap.get(s1);
                //有这个记录更新
                if (oldmap != null) {
                    Integer clic = (Integer) oldmap.get(MccUtil.CLIC_STR);
                    //有这个记录但是没有这个点击
                    if (clic != null) {
                        sumMap.get(s1).put(MccUtil.CLIC_STR, count + clic);
                    } else {
                        sumMap.get(s1).put(MccUtil.CLIC_STR, count);
                    }
                } else {//没有这个记录put
                    oldmap = new ConcurrentHashMap<>();
                    oldmap.put(MccUtil.CLIC_STR, count);
                    sumMap.put(s1, oldmap);
                }
            }
        }
    }

    /**
     * 汇总信息settle
     *
     * @param map
     */
    @Override
    public synchronized void inworkSettle(Map<String, BigDecimal> map) {
        for (String s : map.keySet()) {
            //花费数据
            BigDecimal cost = map.get(s);
            ConcurrentHashMap<String, Object> oldmap = sumMap.get(s);
            //有这个记录更新
            if (oldmap != null) {
                BigDecimal oldCost = (BigDecimal) oldmap.get(COST_STR);
                //有这个记录但是没有这个点击
                if (oldCost != null) {
                    sumMap.get(s).put(COST_STR, oldCost.add(cost));
                } else {
                    sumMap.get(s).put(COST_STR, cost);
                }
            } else {//没有这个记录put
                oldmap = new ConcurrentHashMap<>();
                oldmap.put(MccUtil.COST_STR, cost);
                sumMap.put(s, oldmap);
            }
        }
    }
}
