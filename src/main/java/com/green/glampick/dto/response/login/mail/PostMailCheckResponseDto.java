package com.green.glampick.dto.response.login.mail;

import com.green.glampick.common.response.ResponseCode;
import com.green.glampick.common.response.ResponseMessage;
import com.green.glampick.dto.ResponseDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class PostMailCheckResponseDto extends ResponseDto {

    private boolean authCheck;

    private PostMailCheckResponseDto(boolean authCheck) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.authCheck = authCheck;
    }

    private PostMailCheckResponseDto(String code, String message, boolean authCheck) {
        super(code, message);
        this.authCheck = authCheck;
    }

    public static ResponseEntity<PostMailCheckResponseDto> success() {
        PostMailCheckResponseDto result = new PostMailCheckResponseDto(true);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}