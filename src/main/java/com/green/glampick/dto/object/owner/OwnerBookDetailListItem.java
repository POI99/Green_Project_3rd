package com.green.glampick.dto.object.owner;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OwnerBookDetailListItem {
    private String inputName;
    private String roomName;
    private Long personnel;
    private Long payAmount;
    private String checkInDate;
    private String checkOutDate;

}
