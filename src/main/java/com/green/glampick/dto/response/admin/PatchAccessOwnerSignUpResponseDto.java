package com.green.glampick.dto.response.admin;

import com.green.glampick.common.response.ResponseCode;
import com.green.glampick.common.response.ResponseMessage;
import com.green.glampick.dto.ResponseDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class PatchAccessOwnerSignUpResponseDto extends ResponseDto {

    private PatchAccessOwnerSignUpResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<ResponseDto> success() {
        ResponseDto result = new ResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
