package com.green.glampick.dto.request.owner;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.BindParam;

@Getter
@Setter
public class OwnerWithdrawRequestDto {
    @JsonIgnore
    private Long ownerId;
    @NotNull(message = "글램핑 PK를 입력해주세요.")
    @Schema(example = "1")
    @Parameter(name = "glamp_id")
    private Long glampId;

    public OwnerWithdrawRequestDto(@BindParam("glamp_id") Long glampId) {
        this.glampId = glampId;
    }


}
