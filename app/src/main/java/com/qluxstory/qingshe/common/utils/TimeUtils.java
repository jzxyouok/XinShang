package com.qluxstory.qingshe.common.utils;

/**
 * 随机获取时间戳
 */
public class TimeUtils {

    public static String getSignTime(){
        long t=System.currentTimeMillis();
        String st=(t+"").substring(0,11).trim();
        return st;
    }

}
