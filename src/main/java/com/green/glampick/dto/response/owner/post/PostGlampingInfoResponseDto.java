package com.green.glampick.dto.response.owner.post;

import com.green.glampick.dto.ResponseDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
public class PostGlampingInfoResponseDto extends ResponseDto {

    // 스웨거 보여주기 용
    private long glampId;

    private PostGlampingInfoResponseDto(long glampId) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.glampId = glampId;
    }

    public static ResponseEntity<PostGlampingInfoResponseDto> success(long glampId) {
        PostGlampingInfoResponseDto result = new PostGlampingInfoResponseDto(glampId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


}
