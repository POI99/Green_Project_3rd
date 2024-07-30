package com.green.glampick.dto.response.user;

import com.green.glampick.common.response.ResponseCode;
import com.green.glampick.common.response.ResponseMessage;
import com.green.glampick.dto.ResponseDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class PostUserPasswordResponseDto extends ResponseDto {

    private boolean checkPw;

    private PostUserPasswordResponseDto(boolean checkPw) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.checkPw = checkPw;
    }

    public static ResponseEntity<PostUserPasswordResponseDto> success() {
        PostUserPasswordResponseDto result = new PostUserPasswordResponseDto(true);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
