package com.green.glampick.dto.response.owner.get;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.dto.response.owner.patch.PatchOwnerPeakResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
@ToString
public class GetGlampingPeakPeriodResponseDto extends ResponseDto {
    private String startPeakDate;
    private String endPeakDate;

    private GetGlampingPeakPeriodResponseDto(String startPeakDate, String endPeakDate) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.startPeakDate = startPeakDate;
        this.endPeakDate = endPeakDate;
    }

    public static ResponseEntity<GetGlampingPeakPeriodResponseDto> success(String startPeakDate, String endPeakDate) {
        GetGlampingPeakPeriodResponseDto result = new GetGlampingPeakPeriodResponseDto(startPeakDate,endPeakDate);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
