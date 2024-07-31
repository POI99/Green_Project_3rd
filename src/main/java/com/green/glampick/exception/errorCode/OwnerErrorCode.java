package com.green.glampick.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OwnerErrorCode implements ErrorCode {

    // 400
    WG(HttpStatus.BAD_REQUEST, "글램핑 ID를 올바르게 입력해주세요."), // WRONG_GLAMP_ID
    AH(HttpStatus.BAD_REQUEST, "이미 회원님의 계정에 등록된 글램핑 정보가 있습니다."), // ALREADY_HAVE
    NF(HttpStatus.BAD_REQUEST, "사진을 찾지 못했습니다."), // NOT_FOUND_FILE
    DL(HttpStatus.BAD_REQUEST, "이미 같은 위치에 등록된 글램핑장이 존재합니다."), // DUPLICATED_LOCATION
    FE(HttpStatus.BAD_REQUEST, "파일을 업로드하는 과정에서 에러가 생겼습니다."),    // FILE_UPLOAD_ERROR
    NMG(HttpStatus.BAD_REQUEST, "사용자가 가진 글램핑과 입력된 글램핑이 일치하지 않습니다.");    // NOT_MATCH_GLAMP
    private final HttpStatus httpStatus;
    private final String message;
}
