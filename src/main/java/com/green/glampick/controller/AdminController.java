package com.green.glampick.controller;

import com.green.glampick.dto.request.admin.PatchAccessOwnerSignUpRequestDto;
import com.green.glampick.dto.request.admin.PostBannerRequestDto;
import com.green.glampick.dto.response.admin.DeleteBannerResponseDto;
import com.green.glampick.dto.response.admin.DeleteExclutionOwnerSignUpResponseDto;
import com.green.glampick.dto.response.admin.PatchAccessOwnerSignUpResponseDto;
import com.green.glampick.dto.response.admin.PostBannerResponseDto;
import com.green.glampick.dto.response.login.PostSignOutResponseDto;
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

    //  관리자 페이지 - 사장님 회원가입 승인 처리하기  //
    @PatchMapping("/access/owner/sign-up")
    @Operation(summary = "사장님 회원가입 승인 처리하기 (김수찬)", description = ACCESS_SIGN_UP_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = ACCESS_SIGN_UP_RESPONSE_ERROR_CODE,
        content = @Content(
                mediaType = "application/json", schema = @Schema(implementation = PatchAccessOwnerSignUpResponseDto.class)
        ))
    public ResponseEntity<? super PatchAccessOwnerSignUpResponseDto> accessSignUp
            (@RequestBody PatchAccessOwnerSignUpRequestDto dto) {
        return service.accessSignUp(dto);
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

}
