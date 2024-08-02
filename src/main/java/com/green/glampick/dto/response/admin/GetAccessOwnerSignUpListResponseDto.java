package com.green.glampick.dto.response.admin;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.entity.OwnerEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
public class GetAccessOwnerSignUpListResponseDto extends ResponseDto {

    private List<OwnerEntity> ownerEntities;

    private GetAccessOwnerSignUpListResponseDto(List<OwnerEntity> ownerEntities) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.ownerEntities = ownerEntities;
    }

    public static ResponseEntity<GetAccessOwnerSignUpListResponseDto> success(List<OwnerEntity> ownerEntities) {
        GetAccessOwnerSignUpListResponseDto result = new GetAccessOwnerSignUpListResponseDto(ownerEntities);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
