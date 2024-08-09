package com.green.glampick.dto.request.owner;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PatchOwnerPeakRequestDto {

    @Schema(example = "2024-08-08")
    @NotBlank(message = "peakStartDay 값이 입력되지않았습니다.")
    private String peakStartDay;

    @Schema(example = "2024-08-31")
    @NotBlank(message = "peakEndDay 값이 입력되지않았습니다.")
    private String peakEndDay;

    @Schema(example = "25")
    @NotNull(message = "peakCost 값이 입력되지않았습니다")
    private Integer peakCost;

    @JsonIgnore
    private Long ownerId;
}
