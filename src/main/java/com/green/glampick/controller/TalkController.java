package com.green.glampick.controller;

import com.green.glampick.dto.response.login.PostSignUpResponseDto;
import com.green.glampick.dto.response.talk.PostKakaoTalkResponseDto;
import com.green.glampick.service.TalkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kakao/send")
@Tag(name = "알림톡 컨트롤러 (3차)")
public class TalkController {
    private final TalkService service;

    @PostMapping
    @Operation(summary = "카카오 알림톡 보내기 (김수찬, 김민지)", description = "<strong>입력한 전화번호로 카카오 알림톡이 발송됩니다.",
                responses = {@ApiResponse(
                    responseCode = "200",
                    description = "성공에 대한 반환 값 입니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostKakaoTalkResponseDto.class)
                    ))})
    public ResponseEntity<? super PostKakaoTalkResponseDto> postKakaoTalk() {
        ResponseEntity<? super PostKakaoTalkResponseDto> response = service.postKakaoTalk();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
