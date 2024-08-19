package com.green.glampick.dto.request.login;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.glampick.common.Role;
import com.green.glampick.security.SignInProviderType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpSnsRequestDto {

    private String providerId;
    private String userName;
    private String userPhone;
    private String userNickname;

}
