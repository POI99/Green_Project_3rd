package com.green.glampick.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AdminErrorCode implements ErrorCode{

    // 400
    NFB(HttpStatus.BAD_REQUEST, "배너를 찾을 수 없습니다."),
    UFF(HttpStatus.BAD_REQUEST, "저장가능한 이미지 수를 초과하였습니다."); // DUPLICATED_BOOK

    private final HttpStatus httpStatus;
    private final String message;

}
