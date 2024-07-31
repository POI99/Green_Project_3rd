package com.green.glampick.jin.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.glampick.entity.GlampingEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewGetStarRequestDto {
    @JsonIgnore
    private long userId;
    private long glampId;
}
