package com.green.glampick.dto.response.login;

import com.green.glampick.common.response.ResponseCode;
import com.green.glampick.common.response.ResponseMessage;
import com.green.glampick.dto.ResponseDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class PostOwnerSignInResponseDto extends ResponseDto {

    private String accessToken;

    private PostOwnerSignInResponseDto(String accessToken) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.accessToken = accessToken;
    }

    public static ResponseEntity<PostOwnerSignInResponseDto> success(String accessToken) {
        PostOwnerSignInResponseDto result = new PostOwnerSignInResponseDto(accessToken);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
