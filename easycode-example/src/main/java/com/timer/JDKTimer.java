package com.timer;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class JDKTimer {

	public static void main(String[] args) {

		Timer timer = new Timer();

		timer.schedule(new TimerTask() {
			@Override
			public void run() {
			}
		}, 0, 1000);

		
		
	}
	
	public static void TimerTimeIntavle(){
        //间隔时间
        long cc = 24 * 60 * 60 * 1000;
        //开始时间
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9); //9点
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date date = calendar.getTime();
        //如果第一次执行定时任务的时间 小于当前的时间
        System.out.println(date);
        //安排指定的任务在指定的时间开始进行重复的固定延迟执行。
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                //
            }
        }, date, cc);
	}

}
