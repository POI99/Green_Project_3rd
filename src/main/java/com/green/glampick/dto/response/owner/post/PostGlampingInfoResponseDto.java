package com.green.glampick.dto.response.owner.post;

import com.green.glampick.common.response.ResponseCode;
import com.green.glampick.common.response.ResponseMessage;
import com.green.glampick.dto.ResponseDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
@Getter
@Setter
public class PostGlampingInfoResponseDto extends ResponseDto {

    // 스웨거 보여주기 용
    private long glampId;

    private PostGlampingInfoResponseDto(long glampId) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.glampId = glampId;
    }

    public static ResponseEntity<PostGlampingInfoResponseDto> success(long glampId) {
        PostGlampingInfoResponseDto result = new PostGlampingInfoResponseDto(glampId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // 정보가 덜 입력됨
    public static ResponseEntity<ResponseDto> validationFailed(String errorMsg) {
        ResponseDto result = new ResponseDto(ResponseCode.VALIDATION_FAILED, errorMsg);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    // 이미지 업로드 과정에서 오류 발생
    public static ResponseEntity<ResponseDto> fileUploadError() {
        ResponseDto result = new ResponseDto(ResponseCode.FILE_UPLOAD_ERROR, ResponseMessage.FILE_UPLOAD_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

}
