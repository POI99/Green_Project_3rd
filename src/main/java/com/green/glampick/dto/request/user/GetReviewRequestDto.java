package com.green.glampick.dto.request.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.glampick.common.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.green.glampick.common.GlobalConst.PAGING_SIZE;

@Setter
@Getter
@ToString
public class GetReviewRequestDto extends Paging {
    @Schema(example = "0||1")
    private long typeNum;

    @JsonIgnore private Long reviewId;
    @JsonIgnore private long userId;
    @JsonIgnore private Long ownerId;

    public GetReviewRequestDto(Integer page) {
        super(page, PAGING_SIZE);
    }
}
