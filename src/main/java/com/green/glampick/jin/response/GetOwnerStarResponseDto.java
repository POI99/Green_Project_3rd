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
public class GetOwnerStarResponseDto extends ResponseDto {

    private double  starPointAvg;
    private GetOwnerStarResponseDto(double  starPointAvg) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.starPointAvg = starPointAvg;

    }

    public static ResponseEntity<GetOwnerStarResponseDto> success(double  starPointAvg) {
        GetOwnerStarResponseDto result = new GetOwnerStarResponseDto(starPointAvg);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


}
