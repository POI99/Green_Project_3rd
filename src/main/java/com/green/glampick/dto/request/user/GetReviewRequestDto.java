package com.green.glampick.dto.request.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.glampick.common.Paging;
import lombok.Getter;
import lombok.Setter;

import static com.green.glampick.common.GlobalConst.PAGING_SIZE;

@Setter
@Getter
public class GetReviewRequestDto extends Paging {

    @JsonIgnore private Long reviewId;
    @JsonIgnore private long userId;

    public GetReviewRequestDto(Integer page) {
        super(page, PAGING_SIZE);
    }
}
