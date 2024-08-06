package com.green.glampick.dto.object.glamping;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class GlampingListItem {

    @Schema(example = "15", description = "글램핑장의 PK")
    private long glampId;

    @Schema(example = "뉴욕카라반", description = "글램핑장의 이름")
    private String glampName;

    @Schema(example = "aof5eqx.png", description = "글램핑장의 대표 이미지")
    private String glampPic;

    @Schema(example = "4.6", description = "글램핑장의 별점")
    private double starPoint;

    @Schema(example = "123", description = "글램핑장의 리뷰 개수")
    private int reviewCount;

    @Schema(example = "65,000", description = "글램핑장의 예약 가격")
    private int price;
}
