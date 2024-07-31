package com.green.glampick.service;

import com.green.glampick.dto.request.admin.PatchAccessOwnerSignUpRequestDto;
import com.green.glampick.dto.response.admin.PatchAccessOwnerSignUpResponseDto;
import org.springframework.http.ResponseEntity;

public interface AdminService {
    ResponseEntity<? super PatchAccessOwnerSignUpResponseDto> accessSignUp(PatchAccessOwnerSignUpRequestDto dto);
}
