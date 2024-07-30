package com.green.glampick.dto.response.user;

import com.green.glampick.common.response.ResponseCode;
import com.green.glampick.common.response.ResponseMessage;
import com.green.glampick.dto.ResponseDto;
import com.green.glampick.repository.resultset.GetFavoriteGlampingResultSet;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;


@Setter
@Getter
public class GetFavoriteGlampingResponseDto extends ResponseDto {



    private List<GetFavoriteGlampingResultSet> favoritelist;

    private GetFavoriteGlampingResponseDto(List<GetFavoriteGlampingResultSet> favoritelist) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.favoritelist = favoritelist;
    }

    public static ResponseEntity<ResponseDto> success(List<GetFavoriteGlampingResultSet> favoritelist) {
        GetFavoriteGlampingResponseDto result = new GetFavoriteGlampingResponseDto(favoritelist);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


}
