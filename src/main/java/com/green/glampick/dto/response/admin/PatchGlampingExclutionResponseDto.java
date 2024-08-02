package com.green.glampick.dto.response.admin;

import com.green.glampick.common.response.ResponseCode;
import com.green.glampick.common.response.ResponseMessage;
import com.green.glampick.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class PatchGlampingExclutionResponseDto extends ResponseDto {

    private PatchGlampingExclutionResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<PatchGlampingExclutionResponseDto> success() {
        PatchGlampingExclutionResponseDto result = new PatchGlampingExclutionResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
