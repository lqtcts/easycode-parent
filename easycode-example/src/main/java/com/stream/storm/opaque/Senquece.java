package com.stream.storm.opaque;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;
import backtype.storm.tuple.Values;

public class Senquece extends BaseFunction {
	
	private static final Log logger = LogFactory.getLog(Senquece.class);

	private static final long serialVersionUID = 474023451052581446L;
	

	@Override
	public void execute(TridentTuple input, TridentCollector collector) {
		System.out.println("pv-start");
		String str = input.getString(0);
		String[] motionInfos = str.split("\\|");
		if (motionInfos.length == 14) {
			//获取pvuv数据表示  为1的话证明是pvuv数据
			String ty = motionInfos[13];
			if (ty!=null&&"1".equals(ty.trim())) {
				//发送mapid, pv,uv到下一步
				logger.info("需要进行数据处理pv");
				logger.info("data--"+motionInfos[12]+"---"+motionInfos[1]+"--"+ motionInfos[2]);
				String string = motionInfos[1]!=null&&!"".equals(motionInfos[1])?motionInfos[1]:"0";
				String string2 = motionInfos[2]!=null&&!"".equals(motionInfos[2])?motionInfos[2]:"0";
				
				collector.emit(new Values(motionInfos[12], Integer.parseInt(string), Integer.parseInt(string2)));
			}else {
				logger.info("数据不需要进行处理pv" + str);
			}
		} else {
			logger.error("数据错误pv：" + str);
		}
	}
}