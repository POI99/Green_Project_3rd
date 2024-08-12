package com.green.glampick.jin.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class ReviewGetRevenueRequestDto {

    @JsonIgnore
    private long ownerId;
    @Schema(example = "20240701")
    private long startDayId;
    @Schema(example = "20240731")
    private long endDayId;
}
