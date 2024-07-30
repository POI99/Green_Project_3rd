package com.green.glampick.dto.response.login;

import com.green.glampick.common.response.ResponseCode;
import com.green.glampick.common.response.ResponseMessage;
import com.green.glampick.dto.ResponseDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class PostOwnerSignUpResponseDto extends ResponseDto {

    private long userId;

    private PostOwnerSignUpResponseDto(long userId) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.userId = userId;
    }

    public static ResponseEntity<PostOwnerSignUpResponseDto> success(long userId) {
        PostOwnerSignUpResponseDto result = new PostOwnerSignUpResponseDto(userId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> duplicatedEmail() {
        ResponseDto result = new ResponseDto(ResponseCode.DUPLICATE_EMAIL, ResponseMessage.DUPLICATE_EMAIL);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    public static ResponseEntity<ResponseDto> duplicatedNickname() {
        ResponseDto result = new ResponseDto(ResponseCode.DUPLICATE_NICK_NAME, ResponseMessage.DUPLICATE_NICK_NAME);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    public static ResponseEntity<ResponseDto> duplicatedPhoneNumber() {
        ResponseDto result = new ResponseDto(ResponseCode.DUPLICATE_TEL_NUMBER, ResponseMessage.DUPLICATE_TEL_NUMBER);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    public static ResponseEntity<ResponseDto> invalidEmail() {
        ResponseDto result = new ResponseDto(ResponseCode.INVALID_EMAIL, ResponseMessage.INVALID_EMAIL);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    public static ResponseEntity<ResponseDto> invalidPassword() {
        ResponseDto result = new ResponseDto(ResponseCode.INVALID_PASSWORD, ResponseMessage.INVALID_PASSWORD);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    public static ResponseEntity<ResponseDto> invalidPhone() {
        ResponseDto result = new ResponseDto(ResponseCode.INVALID_PHONE, ResponseMessage.INVALID_PHONE);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    public static ResponseEntity<ResponseDto> invalidNickname() {
        ResponseDto result = new ResponseDto(ResponseCode.INVALID_NICKNAME, ResponseMessage.INVALID_NICKNAME);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    public static ResponseEntity<ResponseDto> validationFail() {
        ResponseDto result = new ResponseDto(ResponseCode.VALIDATION_FAILED, ResponseMessage.VALIDATION_FAILED);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

}
