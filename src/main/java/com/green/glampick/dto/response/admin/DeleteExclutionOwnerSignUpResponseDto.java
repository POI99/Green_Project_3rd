package com.green.glampick.dto.response.admin;

import com.green.glampick.common.response.ResponseCode;
import com.green.glampick.common.response.ResponseMessage;
import com.green.glampick.dto.ResponseDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class DeleteExclutionOwnerSignUpResponseDto extends ResponseDto {

    private DeleteExclutionOwnerSignUpResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<DeleteExclutionOwnerSignUpResponseDto> success() {
        DeleteExclutionOwnerSignUpResponseDto result = new DeleteExclutionOwnerSignUpResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
