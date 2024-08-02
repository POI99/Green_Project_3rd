package com.green.glampick.jin.response;

import com.green.glampick.dto.ResponseDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
public class GetGlampingCancelResponseDto extends ResponseDto {

    private String result;

    private GetGlampingCancelResponseDto(String result) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.result = result;
    }

    public static ResponseEntity<GetGlampingCancelResponseDto> success(String result) {
        GetGlampingCancelResponseDto results = new GetGlampingCancelResponseDto(result);
        return ResponseEntity.status(HttpStatus.OK).body(results);
    }


}
