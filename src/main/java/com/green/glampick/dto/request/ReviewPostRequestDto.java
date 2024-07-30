package com.green.glampick.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class ReviewPostRequestDto {
    private long glampId;
    private long reviewId;

    @JsonIgnore
    private long userId;
    private String reviewOwnerContent;

}
