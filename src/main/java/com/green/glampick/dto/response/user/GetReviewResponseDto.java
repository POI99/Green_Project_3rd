package com.green.glampick.dto.response.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.glampick.common.response.ResponseCode;
import com.green.glampick.common.response.ResponseMessage;
import com.green.glampick.dto.ResponseDto;
import com.green.glampick.dto.object.ReviewListItem;
import com.green.glampick.dto.object.UserReviewListItem;
import com.green.glampick.entity.ReviewImageEntity;
import com.green.glampick.repository.resultset.GetUserReviewResultSet;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Setter
@Getter

public class GetReviewResponseDto extends ResponseDto {

    long TotalReviewsCount;
    List<UserReviewListItem> reviewListItems;

    private GetReviewResponseDto(long totalReviewsCount, List<UserReviewListItem> reviewListItems) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.TotalReviewsCount = totalReviewsCount;
        this.reviewListItems = reviewListItems;
    }
    public GetReviewResponseDto(List<UserReviewListItem> reviewListItems) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.reviewListItems = reviewListItems;
    }

    public static ResponseEntity<GetReviewResponseDto> success(long totalReviewsCount, List<UserReviewListItem> reviewListItems) {
        GetReviewResponseDto result = new GetReviewResponseDto(totalReviewsCount, reviewListItems);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    public static ResponseEntity<GetReviewResponseDto> success(List<UserReviewListItem> reviewListItems) {
        GetReviewResponseDto result = new GetReviewResponseDto(reviewListItems);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
