package com.green.glampick.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OwnerErrorCode implements ErrorCode {

    // 400
    WG(HttpStatus.BAD_REQUEST, "글램핑 ID를 올바르게 입력해주세요."); // WRONG_GLAMP_ID

    private final HttpStatus httpStatus;
    private final String message;
}
