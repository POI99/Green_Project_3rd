package com.green.glampick.jin.response;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.jin.object.GetGlampingHeart;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
public class GetGlampingHeartResponseDto extends ResponseDto {

    List<GetGlampingHeart> getGlampingHearts;
    private GetGlampingHeartResponseDto(List<GetGlampingHeart> getGlampingHearts) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.getGlampingHearts = getGlampingHearts;
    }

    public static ResponseEntity<GetGlampingHeartResponseDto> success(List<GetGlampingHeart> getGlampingHearts) {
        GetGlampingHeartResponseDto result = new GetGlampingHeartResponseDto(getGlampingHearts);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}

