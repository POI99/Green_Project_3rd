package com.green.glampick.controller;

import com.green.glampick.dto.request.login.OwnerSignUpRequestDto;
import com.green.glampick.dto.request.login.SignInRequestDto;
import com.green.glampick.dto.request.login.SignUpRequestDto;
import com.green.glampick.dto.request.owner.OwnerSignInRequestDto;
import com.green.glampick.dto.response.login.PostOwnerSignUpResponseDto;
import com.green.glampick.dto.response.login.PostSignInResponseDto;
import com.green.glampick.dto.response.login.PostSignUpResponseDto;
import com.green.glampick.dto.response.login.mail.PostMailCheckResponseDto;
import com.green.glampick.dto.response.login.mail.PostMailSendResponseDto;
import com.green.glampick.dto.response.login.sms.PostSmsCheckResponseDto;
import com.green.glampick.dto.response.login.sms.PostSmsSendResponseDto;
import com.green.glampick.dto.response.login.token.GetAccessTokenResponseDto;
import com.green.glampick.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.green.glampick.common.swagger.description.login.GetAccessTokenSwaggerDescription.ACCESS_TOKEN_DESCRIPTION;
import static com.green.glampick.common.swagger.description.login.PostCheckMailSwaggerDescription.CHECK_MAIL_DESCRIPTION;
import static com.green.glampick.common.swagger.description.login.PostCheckMailSwaggerDescription.CHECK_MAIL_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.login.PostCheckSmsSwaggerDescription.CHECK_SMS_DESCRIPTION;
import static com.green.glampick.common.swagger.description.login.PostCheckSmsSwaggerDescription.CHECK_SMS_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.login.PostSendMailSwaggerDescription.SEND_MAIL_DESCRIPTION;
import static com.green.glampick.common.swagger.description.login.PostSendMailSwaggerDescription.SEND_MAIL_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.login.PostSendSmsSwaggerDescription.SEND_SMS_DESCRIPTION;
import static com.green.glampick.common.swagger.description.login.PostSendSmsSwaggerDescription.SEND_SMS_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.login.PostSignInSwaggerDescription.SIGN_IN_DESCRIPTION;
import static com.green.glampick.common.swagger.description.login.PostSignInSwaggerDescription.SIGN_IN_RESPONSE_ERROR_CODE;
import static com.green.glampick.common.swagger.description.login.PostSignUpSwaggerDescription.SIGN_UP_DESCRIPTION;
import static com.green.glampick.common.swagger.description.login.PostSignUpSwaggerDescription.SIGN_UP_RESPONSE_ERROR_CODE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "로그인 및 회원가입 컨트롤러")
public class LoginController {
    private final LoginService service;

    @PostMapping("/sign-up")
    @Operation(summary = "이메일 회원가입", description = SIGN_UP_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = SIGN_UP_RESPONSE_ERROR_CODE,
        content = @Content(
                mediaType = "application/json", schema = @Schema(implementation = PostSignUpResponseDto.class)))
    public ResponseEntity<? super PostSignUpResponseDto> signUpUser(@RequestBody @Valid SignUpRequestDto dto) {
        return service.signUpUser(dto);
    }

    @PostMapping("/owner/sign-up")
    @Operation(summary = "사장님 회원가입", description = "")
    @ApiResponse(responseCode = "200", description = "", content = @Content())
    public ResponseEntity<? super PostOwnerSignUpResponseDto> signUpOwner(@RequestBody @Valid OwnerSignUpRequestDto dto) {
        return service.signUpOwner(dto);
    }

    @PostMapping("/sign-in")
    @Operation(summary = "이메일 로그인", description = SIGN_IN_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = SIGN_IN_RESPONSE_ERROR_CODE,
       content = @Content(
                mediaType = "application/json", schema = @Schema(implementation = PostSignInResponseDto.class)))
    public ResponseEntity<? super PostSignInResponseDto> signInUser(HttpServletResponse res, @RequestBody @Valid SignInRequestDto dto) {
        return service.signInUser(res, dto);
    }

    @PostMapping("/owner/sign-in")
    @Operation(summary = "사장님 로그인", description = "")
    @ApiResponse(responseCode = "200", description = "", content = @Content())
    public ResponseEntity<? super PostSignInResponseDto> signInOwner(HttpServletResponse res, @RequestBody @Valid OwnerSignInRequestDto dto) {
        return service.signInOwner(res, dto);
    }

    @GetMapping("/access-token")
    @Operation(summary = "Access Token 불러오기", description = ACCESS_TOKEN_DESCRIPTION)
    @ApiResponse(responseCode = "200",
        content = @Content(
                mediaType = "application/json", schema = @Schema(implementation = GetAccessTokenResponseDto.class)))
    public ResponseEntity<? super GetAccessTokenResponseDto> getAccessToken(HttpServletRequest req) {
        return service.getAccessToken(req);
    }

    @PostMapping("/send-sms")
    @Operation(summary = "휴대폰 인증 문자 보내기", description = SEND_SMS_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = SEND_SMS_RESPONSE_ERROR_CODE,
        content = @Content(
                mediaType = "application/json", schema = @Schema(implementation = PostSmsSendResponseDto.class)))
    public ResponseEntity<? super PostSmsSendResponseDto> sendOne(@RequestParam String userPhone) {
        return service.sendOne(userPhone);
    }

    @PostMapping("/check-sms")
    @Operation(summary = "휴대폰 인증코드 체크", description = CHECK_SMS_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = CHECK_SMS_RESPONSE_ERROR_CODE,
        content = @Content(
                mediaType = "application/json", schema = @Schema(implementation = PostSmsCheckResponseDto.class)))
    public ResponseEntity<? super PostSmsCheckResponseDto> smsCheck(
            @RequestParam String userPhone, @RequestParam int phoneKey)
    {
        return service.checkPhone(userPhone, phoneKey);
    }

    @PostMapping("/mail-send")
    @Operation(summary = "이메일 인증 보내기", description = SEND_MAIL_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = SEND_MAIL_RESPONSE_ERROR_CODE,
        content = @Content(
                mediaType = "application/json", schema = @Schema(implementation = PostMailSendResponseDto.class)))
    public ResponseEntity<? super PostMailSendResponseDto> sendMail(@RequestParam String userEmail) {
        return service.sendAuthCode(userEmail);
    }

    @PostMapping("/mail-check")
    @Operation(summary = "이메일 인증확인", description = CHECK_MAIL_DESCRIPTION)
    @ApiResponse(responseCode = "200", description = CHECK_MAIL_RESPONSE_ERROR_CODE,
        content = @Content(
                mediaType = "application/json", schema = @Schema(implementation = PostMailCheckResponseDto.class)))
    public ResponseEntity<? super PostMailCheckResponseDto> mailCheck(
            @RequestParam String userEmail, @RequestParam int emailKey)
    {
        return service.checkCode(userEmail, emailKey);
    }

}