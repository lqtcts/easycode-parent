package com.logread.handle;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

public interface IHandle {

	public void handle(String line)  ;
    public Map<String, Integer> getMap();
    public Map<String, BigDecimal> getCostMap();
}