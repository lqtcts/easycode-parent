package com.stream.storm.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ContextManager {

	private static ApplicationContext context;
	
	public static ApplicationContext getContext() {
		if(context == null) {
			synchronized (ContextManager.class) {
				if(context == null) {
					context = new ClassPathXmlApplicationContext("conf/applicationContext-storm.xml");
				}
			}
		}
		return context;
	}
}
