package com.green.glampick.dto.request.owner;

import com.green.glampick.common.Paging;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.green.glampick.common.GlobalConst.PAGING_SIZE;
@Getter
@Setter
@ToString
public class ReservationGetRequestDto extends Paging {
    @Schema(example = "1", description = "사장님PK")
    private Long ownerId;

    public ReservationGetRequestDto(Integer page) {
        super(page, PAGING_SIZE);
    }
}
