package com.green.glampick.dto.object.owner;

import java.time.LocalDate;


public interface OwnerBookItem {
    String getInputName();
    Long getPersonnel();
    LocalDate getCheckInDate();
    LocalDate getCheckOutDate();
    Long getPayAmount();
    String getRoomName();
}
