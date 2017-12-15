package com.disruptor;

import com.lmax.disruptor.EventHandler;

public class DeliveryReportEventHandler implements EventHandler<DeliveryReportEvent> {

	/**
	 * 三个参数，event就是生产者向Disruptor发布资源生产的事件，sequence是这个事件在ringbuffer中的序列号，endOfBatch指明该事件是不是ringbuffer中的最后一个事件。 在onEvent方法里处理消费者要做的事
	 */
	@Override
	public void onEvent(DeliveryReportEvent event, long sequence, boolean endOfBatch) throws Exception {
		System.out.println(event.getDeliveryReport().getTest());
	}
}