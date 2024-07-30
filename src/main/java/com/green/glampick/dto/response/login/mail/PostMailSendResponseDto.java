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
public class PostMailSendResponseDto extends ResponseDto {

    private int authKey;

    private PostMailSendResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    private PostMailSendResponseDto(int authKey) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.authKey = authKey;
    }

    public static ResponseEntity<ResponseDto> success(int authKey) {
        PostMailSendResponseDto result = new PostMailSendResponseDto(authKey);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}