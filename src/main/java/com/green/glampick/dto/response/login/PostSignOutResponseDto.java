package com.green.glampick.dto.response.login;

import com.green.glampick.common.response.ResponseCode;
import com.green.glampick.common.response.ResponseMessage;
import com.green.glampick.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class PostSignOutResponseDto extends ResponseDto {

    private PostSignOutResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<ResponseDto> success() {
        ResponseDto result = new ResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
