package com.green.glampick.dto.response.glamping.favorite;

import com.green.glampick.common.response.ResponseCode;
import com.green.glampick.common.response.ResponseMessage;
import com.green.glampick.dto.ResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Setter
@Getter
public class GetFavoriteGlampingResponseDto extends ResponseDto {

    @Schema(example = "01", description = "관심 취소 등록 여부")
    private int resultValue;

    private GetFavoriteGlampingResponseDto(int resultValue) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.resultValue = resultValue;
    }

    public static ResponseEntity<GetFavoriteGlampingResponseDto> success(int resultValue) {
        GetFavoriteGlampingResponseDto result = new GetFavoriteGlampingResponseDto(resultValue);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> noPermission() {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_PERMISSION, ResponseMessage.NOT_PERMISSION);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    public static ResponseEntity<ResponseDto> validateUserId() {
        ResponseDto result = new ResponseDto(ResponseCode.CANT_FIND_USER, ResponseMessage.CANT_FIND_USER);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

}
