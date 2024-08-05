package com.green.glampick.dto.response.owner.get;

import lombok.Getter;
import lombok.Setter;


public interface GetOwnerBookCancelCountResponseDto {
    String getCheckInDate();
    Long getCountCancel();
}
