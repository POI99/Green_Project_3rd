package com.green.glampick.dto.response.owner.get;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.repository.resultset.GetReservationBeforeResultSet;
import com.green.glampick.repository.resultset.GetReservationCancelResultSet;
import com.green.glampick.repository.resultset.GetReservationCompleteResultSet;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
public class GetOwnerBookListResponseDto extends ResponseDto {

    private List<GetReservationBeforeResultSet> before;
    private List<GetReservationCompleteResultSet> complete;
    private List<GetReservationCancelResultSet> cancel;

    public GetOwnerBookListResponseDto(List<GetReservationBeforeResultSet> before
        , List<GetReservationCompleteResultSet> complete, List<GetReservationCancelResultSet> cancel) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.before = before;
        this.complete = complete;
        this.cancel = cancel;
    }


    public ResponseEntity<ResponseDto> success(List<GetReservationBeforeResultSet> before
            , List<GetReservationCompleteResultSet> complete, List<GetReservationCancelResultSet> cancel) {
        GetOwnerBookListResponseDto result = new GetOwnerBookListResponseDto(before, complete, cancel);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
