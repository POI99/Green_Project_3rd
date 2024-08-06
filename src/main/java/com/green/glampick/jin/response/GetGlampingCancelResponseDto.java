package com.green.glampick.jin.response;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.jin.object.GetCancelDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
public class GetGlampingCancelResponseDto extends ResponseDto {

    List<GetCancelDto> cancelDtos;
    private String formattedResult;

    private GetGlampingCancelResponseDto(List<GetCancelDto> cancelDtos) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.cancelDtos = cancelDtos;
    }

    public static ResponseEntity<GetGlampingCancelResponseDto> success(List<GetCancelDto> cancelDtos) {
        GetGlampingCancelResponseDto results = new GetGlampingCancelResponseDto(cancelDtos);
        return ResponseEntity.status(HttpStatus.OK).body(results);
    }


}
