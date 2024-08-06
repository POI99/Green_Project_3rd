package com.green.glampick.dto.response.owner.get;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.dto.object.owner.OwnerBookCountListItem;
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

    //    private List<GetReservationBeforeResultSet> before;
//    private List<GetReservationCompleteResultSet> complete;
//    private List<GetReservationCancelResultSet> cancel;
    private List<OwnerBookCountListItem> countList;

    public GetOwnerBookListResponseDto(List<OwnerBookCountListItem> countList) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
//        this.before = before;
//        this.complete = complete;
//        this.cancel = cancel;
        this.countList = countList;
    }


    public ResponseEntity<ResponseDto> success(List<OwnerBookCountListItem> countList) {
        GetOwnerBookListResponseDto result = new GetOwnerBookListResponseDto(countList);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
