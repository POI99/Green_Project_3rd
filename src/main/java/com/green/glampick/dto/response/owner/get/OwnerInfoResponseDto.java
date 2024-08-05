package com.green.glampick.dto.response.owner.get;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.repository.resultset.GetReservationBeforeResultSet;
import com.green.glampick.repository.resultset.GetReservationCancelResultSet;
import com.green.glampick.repository.resultset.GetReservationCompleteResultSet;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
@Setter
public class OwnerInfoResponseDto extends ResponseDto {
    @Schema(example = "green502@gmail.com", description = "회원 이메일")
    private String ownerEmail;
    @Schema(example = "김그린", description = "회원 이름")
    private String ownerName;
    @Schema(example = "1234567890", description = "사업자 번호")
    private String businessNumber;
    @Schema(example = "010-1234-5678", description = "회원 전화번호")
    private String ownerPhone;

    public OwnerInfoResponseDto(String ownerEmail, String ownerName, String businessNumber, String ownerPhone){
        this.ownerEmail=ownerEmail;
        this.ownerName=ownerName;
        this.businessNumber=businessNumber;
        this.ownerPhone=ownerPhone;
    }

    public static ResponseEntity<ResponseDto> success(String ownerEmail, String ownerName, String businessNumber, String ownerPhone) {
        OwnerInfoResponseDto result = new OwnerInfoResponseDto(ownerEmail, ownerName, businessNumber, ownerPhone);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
