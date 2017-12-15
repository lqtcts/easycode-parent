package com.stream.storm.util;

public class Const {
//	zookeeper连接字符串  pxene01:2181,pxene02:2181,pxene03:2181,pxene04:2181,pxene05:2181
	public static final String ZK_STR = " pxene01:2181,pxene02:2181,pxene03:2181,pxene04:2181,pxene05:2181";  
	//nimbus主机名
	public static final String NIMBUS_HOST = "pxene03";
	//计数topic
	public static final String COUNTER_TOPIC = "newCounter";
	//计数拓扑名
	public static final String COUNTER_TOPO = "OdinCounterTopo";
}
