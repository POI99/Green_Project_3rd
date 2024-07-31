package com.green.glampick.dto.request.owner.module;

import com.green.glampick.dto.request.owner.RoomPostRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import static com.green.glampick.common.GlobalConst.MAX_PEOPLE;
import static com.green.glampick.common.GlobalConst.MIN_PEOPLE;

public class RoomValidate {

    // 이미지가 들어있는가?
    public static void imgExist(List<MultipartFile> img) {
        if (img == null || img.isEmpty()) {    // 글램핑 이미지
            throw new RuntimeException("사진을 업로드해주세요.");
        }
    }
//
//    // 필요한 데이터가 모두 입력되었는가?
//    public static void isNull(RoomPostRequestDto req){
//        GlampingModule.isNull(req.getRoomName());
//        GlampingModule.isNull(req.getInTime());
//        GlampingModule.isNull(req.getOutTime());
//        GlampingModule.isNull(req.getGlampId());
//        GlampingModule.isNull(req.getPrice());
//        GlampingModule.isNull(req.getPeopleNum());
//        GlampingModule.isNull(req.getPeopleMax());
//    }

    // 인원 정보가 올바른가?
    public static void personnel(int standard, int max){
        if (standard < MIN_PEOPLE || standard > MAX_PEOPLE) {
            throw new RuntimeException("객실의 수용 인원은 2명에서 6명까지 가능합니다.");
        }
        if (max < MIN_PEOPLE || max > MAX_PEOPLE) {
            throw new RuntimeException("객실의 수용 인원은 2명에서 6명까지 가능합니다.");
        }
        if ((max - standard) < 0) {
            throw new RuntimeException("최대 인원은 기준 인원보다 작을 수 없습니다.");
        }
    }

    // 시간 형식이 올바른가?
    public static void timeValidator (String time) throws Exception{
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        LocalTime Time;
        try {
            Time = LocalTime.parse(time, timeFormatter);
        } catch (DateTimeParseException e) {
            throw new Exception("시간 형식이 올바르지 않습니다.");
        }
    }

}
