package com.green.glampick.module;


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
    public static boolean week(LocalDate localDate){
        Date date = Timestamp.valueOf(localDate.atStartOfDay());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return day == 6 || day == 7;
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
