package com.green.glampick.dto.request.owner;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OwnerSignInRequestDto {

    private String ownerEmail;
    private String ownerPw;
    @JsonIgnore private String providerId;
    @JsonIgnore private String userSocialType;

}
