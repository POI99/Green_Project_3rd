package com.green.glampick.dto.response.admin;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.entity.GlampingEntity;
import com.green.glampick.entity.OwnerEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

public class GetAccessGlampingInfoResponseDto extends ResponseDto {

    private GlampingEntity glampingEntity;

    private GetAccessGlampingInfoResponseDto(GlampingEntity glampingEntity) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.glampingEntity = glampingEntity;
    }

    public static ResponseEntity<GetAccessGlampingInfoResponseDto> success(GlampingEntity glampingEntity) {
        GetAccessGlampingInfoResponseDto result = new GetAccessGlampingInfoResponseDto(glampingEntity);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
