package com.logread.datainwork;


import java.math.BigDecimal;
import java.util.Map;

public interface DataInwork {
    public void inworkCounter(Map<String, Integer> map);
    public void inworkSettle(Map<String, BigDecimal> map);
}
