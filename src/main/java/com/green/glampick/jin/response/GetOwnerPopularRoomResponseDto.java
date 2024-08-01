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

@Getter
@Setter
public class GetOwnerPopularRoomResponseDto extends ResponseDto {


    List<GetPopularRoom> popularRooms;
    private GetOwnerPopularRoomResponseDto(List<GetPopularRoom> popularRooms) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.popularRooms = popularRooms;

    }

    public static ResponseEntity<GetOwnerPopularRoomResponseDto> success(List<GetPopularRoom> popularRooms) {
        GetOwnerPopularRoomResponseDto result = new GetOwnerPopularRoomResponseDto(popularRooms);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> signInFailed() {
        ResponseDto result = new ResponseDto(ResponseCode.SIGN_IN_FAILED, ResponseMessage.SIGN_IN_FAILED);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }

    public static ResponseEntity<ResponseDto> validationFail() {
        ResponseDto result = new ResponseDto(ResponseCode.VALIDATION_FAILED, ResponseMessage.VALIDATION_FAILED);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);

    }
    public static ResponseEntity<ResponseDto> validateUserId() {
        ResponseDto result = new ResponseDto(ResponseCode.CANT_FIND_USER, ResponseMessage.CANT_FIND_USER);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

}
