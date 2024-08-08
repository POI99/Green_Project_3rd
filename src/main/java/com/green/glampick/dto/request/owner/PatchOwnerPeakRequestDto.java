package com.green.glampick.dto.request.owner;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PatchOwnerPeakRequestDto {
    @Schema(example = "2024-08-08")
    private String peakStartDay;
    @Schema(example = "2024-08-31")
    private String peakEndDay;
    @Schema(example = "25")
    private Integer peakCost;

    @JsonIgnore
    private Long ownerId;
}
