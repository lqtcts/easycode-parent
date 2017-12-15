package com.stream.flume.flume2kafka.source;

import com.stream.flume.flume2kafka.util.GlobalUtil;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class KafkaFrameDecoder extends FrameDecoder {
    private final static Logger logger = LoggerFactory.getLogger(KafkaFrameDecoder.class);

    private final static int DATA_LENGTH = 4;

    private byte[] dataBytes = new byte[DATA_LENGTH];

    private final static int TIME_LENGTH = 8;

    protected Object decode(ChannelHandlerContext ctx, Channel channel,
                            ChannelBuffer buffer) throws Exception {
        if (buffer.readableBytes() < DATA_LENGTH) {
            logger.warn("buffer length is less than 4 bytes");
            return null;
        }
        buffer.markReaderIndex();
        buffer.readBytes(dataBytes, 0, DATA_LENGTH);
        int dataLength = new GlobalUtil().byteArrayToInt(dataBytes);
        buffer.resetReaderIndex();
        int initialBytesToStrip = TIME_LENGTH + dataLength;
        if (buffer.readableBytes() < initialBytesToStrip + DATA_LENGTH) {
            logger.warn("buffer length is less than needed bytes");
            return null;
        }
        buffer.skipBytes(DATA_LENGTH );
        int readerIndex = buffer.readerIndex();
        ChannelBuffer frame = extractFrame(buffer, readerIndex, initialBytesToStrip);
        buffer.readerIndex(readerIndex +initialBytesToStrip);
        return frame;
    }

    protected ChannelBuffer extractFrame(ChannelBuffer buffer, int index, int length) {
        ChannelBuffer frame = buffer.factory().getBuffer(length);
        frame.writeBytes(buffer, index, length);
        return frame;
    }
}
