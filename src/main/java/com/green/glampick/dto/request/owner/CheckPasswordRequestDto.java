package com.green.glampick.dto.request.owner;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckPasswordRequestDto {
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Schema(example = "green502!")
    private String password;
    @NotNull(message = "사장님 PK를 입력해주세요.")
    @Schema(example = "1")
    private Long ownerId;
}
