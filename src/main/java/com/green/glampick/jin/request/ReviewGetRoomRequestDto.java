package com.green.glampick.jin.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewGetRoomRequestDto {
    @JsonIgnore private long ownerId;
    @JsonIgnore
    private long glampId;
    private long startDayId;
    private long endDayId;
}
