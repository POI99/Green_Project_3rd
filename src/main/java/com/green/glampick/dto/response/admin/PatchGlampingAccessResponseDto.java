package com.green.glampick.dto.response.admin;

import com.green.glampick.common.response.ResponseCode;
import com.green.glampick.common.response.ResponseMessage;
import com.green.glampick.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class PatchGlampingAccessResponseDto extends ResponseDto {

    private PatchGlampingAccessResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<PatchGlampingAccessResponseDto> success() {
        PatchGlampingAccessResponseDto result = new PatchGlampingAccessResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
