/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.stream.flume.flume2kafka.source;

import com.google.common.annotations.VisibleForTesting;
import com.stream.flume.flume2kafka.util.GlobalUtil;
import org.apache.flume.ChannelException;
import org.apache.flume.Context;
import org.apache.flume.CounterGroup;
import org.apache.flume.Event;
import org.apache.flume.EventDrivenSource;
import org.apache.flume.conf.Configurable;
import org.apache.flume.conf.Configurables;
import org.apache.flume.source.AbstractSource;
import org.apache.flume.source.SyslogSourceConfigurationConstants;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.DefaultChannelPipeline;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class KafkaSource extends AbstractSource implements EventDrivenSource,
		Configurable {

	private static final Logger logger = LoggerFactory.getLogger(KafkaSource.class);
	
	private int port;
	private String host = null;
	private Channel nettyChannel;
	private Integer eventSize;
	private Map<String, String> prop;
	private CounterGroup counterGroup = new CounterGroup();
	private Boolean keepFields;

	public class KafkaHandler extends SimpleChannelHandler {
		private GlobalUtil GlobalUtil = new GlobalUtil();

		public void setEventSize(int eventSize) {
			GlobalUtil.setEventSize(eventSize);
		}

		public void setKeepFields(boolean keepFields) {
			GlobalUtil.setKeepFields(keepFields);
		}

		public void setFormater(Map<String, String> prop) {
			GlobalUtil.addFormats(prop);
		}

		public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
				throws Exception {
			logger.debug("ChannelHandlerContext is " + ctx.getName());
			logger.debug("exception is " + e.toString());
		}

		@Override
		public void messageReceived(ChannelHandlerContext ctx,
				MessageEvent mEvent) {
			ChannelBuffer buffer = (ChannelBuffer) mEvent.getMessage();
			byte[] timeBytes = new byte[8];
			buffer.readBytes(timeBytes, 0, 8);
			long time = GlobalUtil.byteArrayToLong(timeBytes);
			Event e = null;
			try {
				byte[] data = new byte[buffer.readableBytes()];
                buffer.readBytes(data, 0,buffer.readableBytes());
				e = GlobalUtil.buildMessage(time, data);
			} catch (Exception ex) {
				logger.error("flume receive data error " + ex.toString());
			}

			try {
				getChannelProcessor().processEvent(e);
				counterGroup.incrementAndGet("events.success");
			} catch (ChannelException ex) {
				counterGroup.incrementAndGet("events.dropped");
				logger.error("Error writting to channel, event dropped", ex);
			}
		}
	}

	@Override
	public void start() {
		ChannelFactory factory = new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());
		ServerBootstrap serverBootstrap = new ServerBootstrap(factory);
		serverBootstrap.setOption("reuseAddress", true);// 端口重用
		serverBootstrap.setOption("child.tcpNoDelay", true);// 无延迟
		serverBootstrap.setOption("child.receiveBufferSize", 2048 * 1000);// 设置接收缓冲区大小
		serverBootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() {
				ChannelPipeline pipeline = new DefaultChannelPipeline();
				KafkaHandler handler = new KafkaHandler();
				handler.setEventSize(eventSize);
				handler.setFormater(prop);
				handler.setKeepFields(keepFields);
				pipeline.addLast("frameDecode", new KafkaFrameDecoder());
				pipeline.addLast("handler", handler);
				return pipeline;
			}
		});
		logger.info("Syslog TCP Source starting...");

		if (host == null) {
			nettyChannel = serverBootstrap.bind(new InetSocketAddress(port));
		} else {
			nettyChannel = serverBootstrap.bind(new InetSocketAddress(host,
					port));
		}
		super.start();
	}

	@Override
	public void stop() {
		logger.info("Syslog TCP Source stopping...");
		logger.info("Metrics:{}", counterGroup);

		if (nettyChannel != null) {
			nettyChannel.close();
			try {
				nettyChannel.getCloseFuture().await(60, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				logger.warn("netty server stop interrupted", e);
			} finally {
				nettyChannel = null;
			}
		}

		super.stop();
	}

	public void configure(Context context) {
		Configurables.ensureRequiredNonNull(context,
				SyslogSourceConfigurationConstants.CONFIG_PORT);
		port = context
				.getInteger(SyslogSourceConfigurationConstants.CONFIG_PORT);
		host = context
				.getString(SyslogSourceConfigurationConstants.CONFIG_HOST);
		eventSize = context.getInteger("eventSize", GlobalUtil.DEFAULT_SIZE);
		prop = context
				.getSubProperties(SyslogSourceConfigurationConstants.CONFIG_FORMAT_PREFIX);
		keepFields = context.getBoolean(
				SyslogSourceConfigurationConstants.CONFIG_KEEP_FIELDS, false);
	}

	@VisibleForTesting
	public int getSourcePort() {
		SocketAddress localAddress = nettyChannel.getLocalAddress();
		if (localAddress instanceof InetSocketAddress) {
			InetSocketAddress addr = (InetSocketAddress) localAddress;
			return addr.getPort();
		}
		return 0;
	}

}
