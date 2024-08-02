package com.green.glampick.dto.response.login.sms;

import com.green.glampick.dto.ResponseDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter

public class PostSmsSendResponseDto extends ResponseDto{

    private int phoneKey;

    private PostSmsSendResponseDto(int phoneKey) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.phoneKey = phoneKey;
    }

    public static ResponseEntity<ResponseDto> success(int authKey) {
        PostSmsSendResponseDto result = new PostSmsSendResponseDto(authKey);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


}
