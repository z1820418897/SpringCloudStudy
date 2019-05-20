package com.gc.util;

import com.gc.bean.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class DateFormat {

    public static SimpleDateFormat simpleDateFormat_S = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    public static SimpleDateFormat simpleDateFormat_d = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat simpleDateFormat_M = new SimpleDateFormat("yyyy-MM");



    public static Long date_to_long(Date date) {
        long time = date.getTime();
        return time;
    }



    public static String date_to_str(Date date) {
        String format = simpleDateFormat_S.format(date);
        return format;
    }

    public static String long_to_str(Long date) {
        String format = simpleDateFormat_S.format(long_to_date(date));
        return format;
    }

    public static Date long_to_date(Long date) {
        Date date1 = new Date(date);
        return date1;
    }

    public static Long str_to_Long_day(String date,int day) throws ParseException {
        long l = str_to_long(date) + (day * 60 * 60 * 24 * 1000);
        return l;
    }

    ///-------------------------------------------------------------------------------------
    public static Long str_to_long(String date) throws ParseException {
        Long aLong = date_to_long(str_to_date(date));
        return aLong;
    }

    public static Date str_to_date(String date) throws ParseException {
        Date parse = simpleDateFormat_S.parse(date);
        return parse;
    }


    /**
     * 获取一天的最大和最小时间戳
     * */
    public static DateUtil JudgeByD(String date) throws ParseException {
        DateUtil dateUtil = new DateUtil();

        Date parse = simpleDateFormat_d.parse(date);

        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(parse.getTime()), ZoneId.systemDefault());
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        LocalDateTime overOfDay = localDateTime.with(LocalTime.MAX);

        Date satrt = parse.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
        Date over = parse.from(overOfDay.atZone(ZoneId.systemDefault()).toInstant());

        dateUtil.setStartDate(satrt.getTime());
        dateUtil.setOverDate(over.getTime());

        return dateUtil;
    }

    /**
     * 获取一个月的最大和最小时间戳
     * */
    public static DateUtil JudgeByM(String date) throws ParseException {
        DateUtil dateUtil = new DateUtil();

        Date parse=simpleDateFormat_M.parse(date);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parse);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date minDate = calendar.getTime();
        LocalDateTime startLocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(minDate.getTime()), ZoneId.systemDefault());
        LocalDateTime startOfDay = startLocalDateTime.with(LocalTime.MIN);
        Date satrt = minDate.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
        dateUtil.setStartDate(satrt.getTime());

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date maxDate = calendar.getTime();
        LocalDateTime overLocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(maxDate.getTime()), ZoneId.systemDefault());
        LocalDateTime overOfDay = overLocalDateTime.with(LocalTime.MAX);
        Date over = maxDate.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
        dateUtil.setOverDate(over.getTime());

        return dateUtil;
    }


}
