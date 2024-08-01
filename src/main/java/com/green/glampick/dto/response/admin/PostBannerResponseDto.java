package com.green.glampick.dto.response.admin;

import com.green.glampick.common.response.ResponseCode;
import com.green.glampick.common.response.ResponseMessage;
import com.green.glampick.dto.ResponseDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
@Setter
public class PostBannerResponseDto extends ResponseDto {

    private PostBannerResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<PostBannerResponseDto> success() {
        PostBannerResponseDto result = new PostBannerResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
