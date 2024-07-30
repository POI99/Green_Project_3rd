package com.green.glampick.dto.response.glamping;

import com.green.glampick.common.response.ResponseCode;
import com.green.glampick.common.response.ResponseMessage;
import com.green.glampick.dto.ResponseDto;
import com.green.glampick.dto.object.glamping.GlampingRoomListItem;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
@Setter
@ToString
@SuperBuilder
public class GetMoreRoomItemResponseDto extends ResponseDto {
    private List<GlampingRoomListItem> roomItems;

    private GetMoreRoomItemResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }
    public static ResponseEntity<ResponseDto> success() {
        ResponseDto result = new ResponseDto(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public GetMoreRoomItemResponseDto(List<GlampingRoomListItem> roomItems) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.roomItems = roomItems;
    }
}
