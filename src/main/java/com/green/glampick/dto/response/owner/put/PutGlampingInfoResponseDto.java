package com.green.glampick.dto.response.owner.put;

import com.green.glampick.common.response.ResponseCode;
import com.green.glampick.common.response.ResponseMessage;
import com.green.glampick.dto.ResponseDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class PutGlampingInfoResponseDto extends ResponseDto {


    private PutGlampingInfoResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    // 성공
    public static ResponseEntity<ResponseDto> success() {
        ResponseDto result = new ResponseDto(ResponseCode.SUCCESS, "수정을 완료하였습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 정보가 덜 입력됨
    public static ResponseEntity<ResponseDto> validationFailed(String errorMsg) {
        ResponseDto result = new ResponseDto(ResponseCode.VALIDATION_FAILED, errorMsg);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

}
