package com.green.glampick.module;


import com.green.glampick.repository.resultset.GetPeakDateResultSet;

import java.sql.Timestamp;
import java.util.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;

public class DateModule {

    // 현재 날짜 (YYYY-MM-DD)
    public static LocalDate nowDate() {
         return LocalDate.now();
    }


    // 요일 (일~토 1~7) true = 주말(금 토) false = 평일 (일 ~ 목)
    public static boolean isWeekend(LocalDate localDate){
        Date date = Timestamp.valueOf(localDate.atStartOfDay());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return day == 6 || day == 7;
    }
    // 1. 현재 날짜 기준
    public static boolean isWeekend(){
        return isWeekend(nowDate());
    }
    // 2. 입력된 날짜 기준
    public static boolean isWeekend(String stringDate){
        return isWeekend(parseToLocalDate(stringDate));
    }


    // 성수기인지 아닌지 판단 (맞으면 true 틀리면 false)
    public static boolean isPeak(LocalDate date, LocalDate peakStart, LocalDate peakEnd) {
        return !(date.isAfter(peakStart) || !peakEnd.isAfter(date));    // 겹치면 성수기, 겹치면 true
    }
    // 1. 현재 날짜 기준
    public static boolean isPeak(GetPeakDateResultSet peak) {
        return isPeak(nowDate(), peak.getStartDate(), peak.getEndDate());
    }
    // 2. 입력된 날짜 기준
    public static boolean isPeak(String date, GetPeakDateResultSet peak) {
        return isPeak(parseToLocalDate(date), peak.getStartDate(), peak.getEndDate());
    }


    public static LocalDate parseToLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate;
    }

    public static boolean checkDate(String in, String out) {
        return parseToLocalDate(out).isBefore(parseToLocalDate(in)); // 틀리면 true
    }

    public static HashMap<String, LocalDate> parseDate(String in, String out, String dbCheckIn, String dbCheckOut) {
        HashMap<String, LocalDate> date = new HashMap<>();
        //Request check-in check-out
        date.put("inDate", parseToLocalDate(in));
        date.put("outDate", parseToLocalDate(out));

        //비교 할 check-in check-out
        date.put("inDate2", parseToLocalDate(dbCheckIn));
        date.put("outDate2", parseToLocalDate(dbCheckOut));

        return date;
    }

    public static  boolean checkOverlap(HashMap<String, LocalDate> dateHashMap) {
        //get Req in out date
        LocalDate start1 = dateHashMap.get("inDate");
        LocalDate end1 = dateHashMap.get("outDate");

        //get DB in out date
        LocalDate start2 = dateHashMap.get("inDate2");
        LocalDate end2 = dateHashMap.get("outDate2");

        return !start1.isAfter(end2) && !start2.isAfter(end1); // 날짜가 겹치면 true
    }

}
