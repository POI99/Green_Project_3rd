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

    private List<Long> bannerId;

    private PostBannerResponseDto(List<Long> bannerId) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.bannerId = bannerId;
    }

    public static ResponseEntity<PostBannerResponseDto> success(List<Long> bannerId) {
        PostBannerResponseDto result = new PostBannerResponseDto(bannerId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
