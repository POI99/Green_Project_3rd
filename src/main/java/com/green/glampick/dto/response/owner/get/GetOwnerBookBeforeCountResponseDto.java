package com.green.glampick.dto.response.owner.get;

import lombok.Getter;
import lombok.Setter;

public interface GetOwnerBookBeforeCountResponseDto {
    String getCheckInDate();
    Long getCountBefore();

}
