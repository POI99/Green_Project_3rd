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
public class DeleteBannerResponseDto extends ResponseDto {

    private DeleteBannerResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<DeleteBannerResponseDto> success() {
        DeleteBannerResponseDto result = new DeleteBannerResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
