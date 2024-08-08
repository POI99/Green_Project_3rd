package com.green.glampick.module;

import com.green.glampick.security.AuthenticationFacade;

import java.sql.Timestamp;
import java.time.DayOfWeek;
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

    public static boolean checkDate(String in, String out) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate inDate = LocalDate.parse(in, formatter);
        LocalDate outDate = LocalDate.parse(out, formatter);
        return outDate.isBefore(inDate); // 틀리면 true
    }

    public static HashMap<String, LocalDate> parseDate(String in, String out, String dbCheckIn, String dbCheckOut) {
        HashMap<String, LocalDate> date = new HashMap<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //Request check-in check-out
        LocalDate inDate = LocalDate.parse(in, formatter);
        LocalDate outDate = LocalDate.parse(out, formatter);
        date.put("inDate", inDate);
        date.put("outDate", outDate);

        //비교 할 check-in check-out
        LocalDate inDate2 = LocalDate.parse(dbCheckIn, formatter);
        LocalDate outDate2 = LocalDate.parse(dbCheckOut, formatter);
        date.put("inDate2", inDate2);
        date.put("outDate2", outDate2);

        return date;
    }

    public static  boolean checkOverlap(HashMap<String, LocalDate> dateHashMap) {
        //get Req in out date
        LocalDate start1 = dateHashMap.get("inDate");
        LocalDate end1 = dateHashMap.get("outDate");
        System.out.println("start1: " + start1);
        System.out.println("end1: " + end1);
        //get DB in out date
        LocalDate start2 = dateHashMap.get("inDate2");
        LocalDate end2 = dateHashMap.get("outDate2");
        System.out.println("start2: " + start2);
        System.out.println("end2: " + end2);
        System.out.println(!start1.isAfter(end2) && !start2.isAfter(end1));
        return !start1.isAfter(end2) && !start2.isAfter(end1); // 날짜가 겹치면 true
    }

}
