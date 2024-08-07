package com.green.glampick.dto.response.owner.get;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.dto.object.owner.GetRoomItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.List;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;

@Getter
public class GetGlampingInfoResponseDto extends ResponseDto {
    @Schema(example = "true", description = "글램핑 등록 완료 상태")
    private boolean state;

    @Schema(example = "그린 글램핑")
    private String glampName;

    @Schema(example = "0535721005")
    private String glampCall;

    @Schema(example = "glamping.jpg")
    private String glampImage;

    @Schema(example = "대구광역시 중구 109-2")
    private String glampLocation;

    @Schema(example = "gyeongbuk")
    private String region;

    @Schema(example = "10000", description = "인원 추가 요금")
    private Integer extraCharge;

    @Schema(example = "글램핑 소개")
    private String glampIntro;

    @Schema(example = "기본 정보")
    private String infoBasic;

    @Schema(example = "유의사항")
    private String infoNotice;

    @Schema(example = "추가 주차 정보")
    private String traffic;

    @Schema(example = "0", description = "0이면 심사대기, -1이면 심사반려")
    private Integer exclusionStatus;


    public GetGlampingInfoResponseDto(boolean state, String glampName, String glampCall, String glampImage
            , String glampLocation, String region, Integer extraCharge, String glampIntro, String infoBasic
        , String infoNotice, String traffic, Integer exclusionStatus){
        super(SUCCESS_CODE, "글램핑 정보 리스트를 불러왔습니다.");
        this.state=state;
        this.glampName=glampName;
        this.glampCall=glampCall;
        this.glampImage=glampImage;
        this.region=region;
        this.glampLocation=glampLocation;
        this.extraCharge=extraCharge;
        this.glampIntro=glampIntro;
        this.infoBasic=infoBasic;
        this.infoNotice=infoNotice;
        this.traffic=traffic;
        this.exclusionStatus=exclusionStatus;
    }

    public static ResponseEntity<GetRoomListResponseDto> success(List<GetRoomItem> r) {
        GetRoomListResponseDto result = new GetRoomListResponseDto(r);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
