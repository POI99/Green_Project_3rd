package com.green.glampick.dto.response.login.sms;

import com.green.glampick.common.response.ResponseCode;
import com.green.glampick.common.response.ResponseMessage;
import com.green.glampick.dto.ResponseDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class PostSmsCheckResponseDto extends ResponseDto {

    private boolean phoneCheck;

    private PostSmsCheckResponseDto(boolean phoneCheck) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.phoneCheck = phoneCheck;
    }

    public static ResponseEntity<PostSmsCheckResponseDto> success() {
        PostSmsCheckResponseDto result = new PostSmsCheckResponseDto(true);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
