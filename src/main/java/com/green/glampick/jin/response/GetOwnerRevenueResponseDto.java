package com.green.glampick.jin.response;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.jin.object.GetRevenue;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;


@Getter
@Setter
public class GetOwnerRevenueResponseDto extends ResponseDto {


   List<GetRevenue> revenue;

    private GetOwnerRevenueResponseDto(List<GetRevenue> revenue) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.revenue = revenue;

    }

    public static ResponseEntity<GetOwnerRevenueResponseDto> success(List<GetRevenue> revenue) {
        GetOwnerRevenueResponseDto result = new GetOwnerRevenueResponseDto(revenue);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
