package com.green.glampick.controller;

import com.green.glampick.dto.response.admin.*;
import com.green.glampick.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.green.glampick.common.swagger.description.admin.DeleteBannerSwaggerDescription.DELETE_BANNER_DESCRIPTION;
import static com.green.glampick.common.swagger.description.admin.DeleteBannerSwaggerDescription.DELETE_BANNER_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.admin.DeleteExclutionSignUpSwaggerDescription.EXCLUTION_SIGN_UP_DESCRIPTION;
import static com.green.glampick.common.swagger.description.admin.DeleteExclutionSignUpSwaggerDescription.EXCLUTION_SIGN_UP_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.admin.PatchAccessSignUpSwaggerDescription.ACCESS_SIGN_UP_DESCRIPTION;
import static com.green.glampick.common.swagger.description.admin.PatchAccessSignUpSwaggerDescription.ACCESS_SIGN_UP_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.admin.PostBannerSwaggerDescription.POST_BANNER_DESCRIPTION;
import static com.green.glampick.common.swagger.description.admin.PostBannerSwaggerDescription.POST_BANNER_RESPONSE_ERROR_CODE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Tag(name = "관리자 컨트롤러")
public class AdminController {
    private final AdminService service;

    //  관리자 페이지 - 사장님 회원가입 정보 불러오기  //
    public ResponseEntity<? super GetOwnerSignUpResponseDto> getOwnerSignUpInfo (@RequestParam Long ownerId) {
//        return service.getOwnerSignUpInfo(ownerId);
        return null;
    }

    //  관리자 페이지 - 사장님 회원가입 승인 처리하기  //
    @PatchMapping("/access/owner/sign-up")
    @Operation(summary = "사장님 회원가입 승인 처리하기 (김수찬)", description = ACCESS_SIGN_UP_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = ACCESS_SIGN_UP_RESPONSE_ERROR_CODE,
        content = @Content(
                mediaType = "application/json", schema = @Schema(implementation = PatchAccessOwnerSignUpResponseDto.class)
        ))
    public ResponseEntity<? super PatchAccessOwnerSignUpResponseDto> accessSignUp
            (@RequestParam Long ownerId) {
        return service.accessSignUp(ownerId);
    }

    //  관리자 페이지 - 사장님 회원가입 반려 처리하기  //
    @DeleteMapping("/exclution/owner/sign-up")
    @Operation(summary = "반려 처리하기 (김수찬)", description = EXCLUTION_SIGN_UP_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = EXCLUTION_SIGN_UP_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PatchAccessOwnerSignUpResponseDto.class)
            ))
    public ResponseEntity<? super DeleteExclutionOwnerSignUpResponseDto> exclutionSignUp(@RequestParam Long ownerId) {
        return service.exclutionSignUp(ownerId);
    }

    //  관리자 페이지 - 메인 화면 배너 추가하기  //
    @PostMapping(name = "/banner", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "메인 배너 추가하기 (김수찬)", description = POST_BANNER_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = POST_BANNER_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PostBannerResponseDto.class)
            ))
    public ResponseEntity<? super PostBannerResponseDto> postBanner(@RequestPart List<MultipartFile> file) {
        return service.postBanner(file);
    }

    //  관리자 페이지 - 메인 화면 배너 삭제하기  //
    @DeleteMapping("/banner")
    @Operation(summary = "메인 배너 삭제하기 (김수찬)", description = DELETE_BANNER_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = DELETE_BANNER_RESPONSE_ERROR_CODE,
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = DeleteBannerResponseDto.class)
            ))
    public ResponseEntity<? super DeleteBannerResponseDto> deleteBanner(@RequestParam Long bannerId) {
        return service.deleteBanner(bannerId);
    }

    //  관리자 페이지 - 글램핑 등록 승인 처리하기  //
    @PatchMapping("/access/owner/glamping")
    @Operation(summary = "글램핑 등록 승인 처리하기 (김수찬)", description = "")
    @ApiResponse(responseCode = "200", description = "",
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PatchGlampingAccessResponseDto.class)
            ))
    public ResponseEntity<? super PatchGlampingAccessResponseDto> accessGlamping(@RequestParam Long glampId) {
        return service.accessGlamping(glampId);
    }

    //  관리자 페이지 - 글램핑 등록 반려 처리하기  //
    @PatchMapping("/exclution/owner/glamping")
    @Operation(summary = "글램핑 등록 반려 처리하기 (김수찬)", description = "")
    @ApiResponse(responseCode = "200", description = "",
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PatchGlampingExclutionResponseDto.class)
            ))
    public ResponseEntity<? super PatchGlampingExclutionResponseDto> exclutionGlamping(@RequestParam Long glampId) {
        return service.exclutionGlamping(glampId);
    }

}
