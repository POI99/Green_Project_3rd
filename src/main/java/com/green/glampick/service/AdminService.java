package com.green.glampick.service;

import com.green.glampick.dto.request.admin.PatchAccessOwnerSignUpRequestDto;
import com.green.glampick.dto.request.admin.PostBannerRequestDto;
import com.green.glampick.dto.response.admin.DeleteExclutionOwnerSignUpResponseDto;
import com.green.glampick.dto.response.admin.PatchAccessOwnerSignUpResponseDto;
import com.green.glampick.dto.response.admin.PostBannerResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

public interface AdminService {

    ResponseEntity<? super PatchAccessOwnerSignUpResponseDto> accessSignUp(PatchAccessOwnerSignUpRequestDto dto);

    ResponseEntity<? super DeleteExclutionOwnerSignUpResponseDto> exclutionSignUp(Long ownerId);

    ResponseEntity<? super PostBannerResponseDto> postBanner(PostBannerRequestDto dto, MultipartFile file);
}
