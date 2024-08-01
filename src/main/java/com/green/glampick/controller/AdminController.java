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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Tag(name = "관리자 컨트롤러")
public class AdminController {
    private final AdminService service;

    @PatchMapping("/access/owner/sign-up")
    @Operation(summary = "승인 처리하기 (김수찬)", description = "")
    @ApiResponse(responseCode = "200", description = "",
        content = @Content(
                mediaType = "application/json", schema = @Schema(implementation = PatchAccessOwnerSignUpResponseDto.class)
        ))
    public ResponseEntity<? super PatchAccessOwnerSignUpResponseDto> accessSignUp
            (@RequestBody PatchAccessOwnerSignUpRequestDto dto) {
        return service.accessSignUp(dto);
    }

    @DeleteMapping("/exclution/owner/sign-up")
    @Operation(summary = "반려 처리하기 (김수찬)", description = "")
    @ApiResponse(responseCode = "200", description = "",
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PatchAccessOwnerSignUpResponseDto.class)
            ))
    public ResponseEntity<? super DeleteExclutionOwnerSignUpResponseDto> exclutionSignUp(@RequestParam Long ownerId) {
        return service.exclutionSignUp(ownerId);
    }

    @PostMapping(name = "/banner", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "메인 배너 추가하기 (김수찬)", description = "")
    @ApiResponse(responseCode = "200", description = "",
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = PostBannerResponseDto.class)
            ))
    public ResponseEntity<? super PostBannerResponseDto> postBanner(@RequestPart List<MultipartFile> file) {
        return service.postBanner(file);
    }

    @DeleteMapping("/banner")
    @Operation(summary = "메인 배너 삭제하기 (김수찬)", description = "")
    @ApiResponse(responseCode = "200", description = "",
            content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = DeleteBannerResponseDto.class)
            ))
    public ResponseEntity<? super DeleteBannerResponseDto> deleteBanner(@RequestParam Long bannerId) {
        return service.deleteBanner(bannerId);
    }

}
