package com.green.glampick.jin.response;

import com.green.glampick.common.response.ResponseCode;
import com.green.glampick.common.response.ResponseMessage;
import com.green.glampick.dto.ResponseDto;
import com.green.glampick.jin.object.GetPopularRoom;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
public class GetOwnerPopularRoomResponseDto extends ResponseDto {


    List<GetPopularRoom> popularRooms;
    private GetOwnerPopularRoomResponseDto(List<GetPopularRoom> popularRooms) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.popularRooms = popularRooms;

    }

    public static ResponseEntity<GetOwnerPopularRoomResponseDto> success(List<GetPopularRoom> popularRooms) {
        GetOwnerPopularRoomResponseDto result = new GetOwnerPopularRoomResponseDto(popularRooms);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


}
