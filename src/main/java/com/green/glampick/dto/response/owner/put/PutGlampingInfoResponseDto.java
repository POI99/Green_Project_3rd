package com.green.glampick.dto.response.owner.put;

import com.green.glampick.dto.ResponseDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
public class PutGlampingInfoResponseDto extends ResponseDto {


    private PutGlampingInfoResponseDto() {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
    }

    // 성공
    public static ResponseEntity<ResponseDto> success() {
        ResponseDto result = new ResponseDto(SUCCESS_CODE, "수정을 완료하였습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
