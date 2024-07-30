package com.green.glampick.dto.response.user;

import com.green.glampick.common.response.ResponseCode;
import com.green.glampick.common.response.ResponseMessage;
import com.green.glampick.dto.ResponseDto;
import com.green.glampick.entity.ReviewEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class DeleteReviewResponseDto extends ResponseDto {

    private Long reviewId;

    private DeleteReviewResponseDto(ReviewEntity reviewEntity) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.reviewId = reviewEntity.getReviewId();
    }

    public static ResponseEntity<ResponseDto> success() {
        ResponseDto result = new ResponseDto(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
