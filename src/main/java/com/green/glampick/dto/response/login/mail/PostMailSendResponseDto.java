package com.green.glampick.dto.response.login.mail;

import com.green.glampick.dto.ResponseDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
public class PostMailSendResponseDto extends ResponseDto {

    private int authKey;

    private PostMailSendResponseDto() {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
    }

    private PostMailSendResponseDto(int authKey) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.authKey = authKey;
    }

    public static ResponseEntity<ResponseDto> success(int authKey) {
        PostMailSendResponseDto result = new PostMailSendResponseDto(authKey);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}