package com.green.glampick.dto.request.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.glampick.common.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatchAccessOwnerSignUpRequestDto {

    private Long ownerId;

}
