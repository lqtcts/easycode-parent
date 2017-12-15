package com.stream.storm.trident;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import backtype.storm.spout.Scheme;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class MessageScheme implements Scheme { 
    
	private static final long serialVersionUID = 3970158892010397541L;
	
	private static final Log logger = LogFactory.getLog(MessageScheme.class);

	public List<Object> deserialize(byte[] ser) {
        try {
            String msg = new String(ser, "UTF-8"); 
            return new Values(msg);
        } catch (UnsupportedEncodingException e) {  
        	logger.error("serial data error", e);
        }
        return null;
    }
    
    public Fields getOutputFields() {
        return new Fields("str");  
    }  
    
} 
