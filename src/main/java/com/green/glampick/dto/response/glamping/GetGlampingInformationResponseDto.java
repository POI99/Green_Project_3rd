package com.green.glampick.dto.response.glamping;

import com.green.glampick.common.response.ResponseCode;
import com.green.glampick.common.response.ResponseMessage;
import com.green.glampick.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class GetGlampingInformationResponseDto extends ResponseDto {

    private GetGlampingInformationResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<ResponseDto> success() {
        ResponseDto result = new ResponseDto(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> noExistedGlamp() {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_GLAMP, ResponseMessage.NOT_EXISTED_GLAMP);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

}
