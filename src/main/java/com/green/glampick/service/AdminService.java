package com.green.glampick.service;

import com.green.glampick.dto.response.admin.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdminService {

    //  관리자 페이지 - 사장님 회원가입 승인 처리하기  //
    ResponseEntity<? super PatchAccessOwnerSignUpResponseDto> accessSignUp(Long ownerId);

    //  관리자 페이지 - 사장님 회원가입 반려 처리하기  //
    ResponseEntity<? super DeleteExclutionOwnerSignUpResponseDto> exclutionSignUp(Long ownerId);

    //  관리자 페이지 - 메인 화면 배너 추가하기  //
    ResponseEntity<? super PostBannerResponseDto> postBanner(List<MultipartFile> file);

    //  관리자 페이지 - 메인 화면 배너 삭제하기  //
    ResponseEntity<? super DeleteBannerResponseDto> deleteBanner(Long bannerId);

    //  관리자 페이지 - 글램핑 등록 승인 처리하기  //
    ResponseEntity<? super PatchGlampingAccessResponseDto> accessGlamping(@RequestParam Long glampId);

    //  관리자 페이지 - 글램핑 등록 반려 처리하기  //
    ResponseEntity<? super PatchGlampingExclutionResponseDto> exclutionGlamping(@RequestParam Long glampId);

}
