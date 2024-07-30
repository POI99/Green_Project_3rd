package com.green.glampick.dto.response.glamping;

import com.green.glampick.common.response.ResponseCode;
import com.green.glampick.common.response.ResponseMessage;
import com.green.glampick.dto.ResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;

@Setter
@Getter
@SuperBuilder
@ToString
public class GetMoreRoomImageResponseDto extends ResponseDto {

    private HashMap<String, List<String>> moreRoomImages;

    public static ResponseEntity<ResponseDto> success() {
        ResponseDto result = new ResponseDto(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> noExistedGlamp() {
        ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_GLAMP, ResponseMessage.NOT_EXISTED_GLAMP);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    public GetMoreRoomImageResponseDto(HashMap<String, List<String>> moreRoomImages) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.moreRoomImages = moreRoomImages;
    }
}
