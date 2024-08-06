package com.green.glampick.dto.object.owner;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class OwnerBookCountListItem {
    private String checkInDate;
    private Long ingCount;
    private Long cancelCount;
    private Long completeCount;

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate.toString();
    }
}
