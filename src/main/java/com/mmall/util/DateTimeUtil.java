package com.mmall.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class DateTimeUtil {
    public static final String STANDARD_FORMAT="yyyy-MM-dd HH:mm:SS";
    // joda-time dateTimeFormatter
    // 功能1  str -> Date
    // 功能2  Date -> str

    /**
     * 把字符串类型转为日期类型
     * @param dateTimeStr
     * @param formatStr
     * @return
     */
    public static Date strToDate(String dateTimeStr , String formatStr){
        // 设置传入的格式
        DateTimeFormatter dateTimeFormatter= DateTimeFormat.forPattern(formatStr);
        DateTime dateTime=dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }public static Date strToDate(String dateTimeStr){
        // 设置传入的格式
        DateTimeFormatter dateTimeFormatter= DateTimeFormat.forPattern(STANDARD_FORMAT);
        DateTime dateTime=dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }
    public static String dateToStr(Date date,String formatStr){
        // 1:判断传入的日期格式是否为空
        if (date == null) {
            return StringUtils.EMPTY;
        }
        DateTime dateTime=new DateTime(date);
        return dateTime.toString(formatStr);
    }
    public static String dateToStr(Date date){
        // 1:判断传入的日期格式是否为空
        if (date == null) {
            return StringUtils.EMPTY;
        }
        DateTime dateTime=new DateTime(date);
        return dateTime.toString(STANDARD_FORMAT);
    }

}
