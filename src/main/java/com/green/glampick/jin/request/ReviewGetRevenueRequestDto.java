package com.green.glampick.jin.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReviewGetRevenueRequestDto {
    @JsonIgnore
    private long ownerId;
}
