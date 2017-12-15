package com.stream.kafka.kafka2hdfs;

/**
 * 
 * read log from kafka to hdfs
 * @author nakedou
 * 2015年3月26日
 *
 */
public class Main {

	public static void main(String[] args) {
		// 旧：String zk = "pxene01:2181,pxene02:2181,pxene03:2181,pxene04:2181,pxene05:2181";
		// 新：String zk = "pxene01:2182,pxene08:2182,pxene09:2182,pxene10:2182,pxene11:2182";
		String zk = "pxene07:2182,pxene08:2182,pxene09:2182,pxene10:2182,pxene11:2182";//线上地址
		String groupid = "pxene";
		String cTopic = "newCounter";
		String sTopic = "newSettle";
		String pTopic = "newPv";
//		String cRoot = "hdfs://pxene01:9000/user/root/odinCounter";
//		String sRoot = "hdfs://pxene01:9000/user/root/odinSettle";
//		String pRoot = "hdfs://pxene01:9000/user/root/odinPv";
		String cRoot = "/user/root/odinCounter";
        String sRoot = "/user/root/odinSettle";
        String pRoot = "/user/root/odinPv";
		int threads = 5; //the same with the num of kafka partition
		
		HDFSWriter cWriter = new HDFSWriter();
		cWriter.init(cTopic, cRoot, zk, groupid, threads,60000);
		
		HDFSWriter sWriter = new HDFSWriter();
		sWriter.init(sTopic, sRoot, zk, groupid, threads,60000);
		
		HDFSWriter pWriter = new HDFSWriter();
		pWriter.init(pTopic, pRoot, zk, groupid, threads,60000);
    }

}
