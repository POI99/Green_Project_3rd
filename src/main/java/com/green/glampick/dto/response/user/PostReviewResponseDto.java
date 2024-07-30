package com.green.glampick.dto.response.user;

import com.green.glampick.common.response.ResponseCode;
import com.green.glampick.common.response.ResponseMessage;
import com.green.glampick.dto.ResponseDto;
import com.green.glampick.entity.ReviewEntity;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PostReviewResponseDto extends ResponseDto {

    private long reviewId;

    private PostReviewResponseDto(long reviewId) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.reviewId = reviewId;
    }

    public static ResponseEntity<PostReviewResponseDto> success(long reviewId) {
        PostReviewResponseDto result = new PostReviewResponseDto(reviewId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }



}
