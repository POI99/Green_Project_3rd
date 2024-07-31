package com.green.glampick.dto.request.login;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminSignInRequestDto {

    private String adminId;
    private String adminPw;

}
