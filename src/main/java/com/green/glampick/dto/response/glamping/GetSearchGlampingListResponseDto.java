package com.green.glampick.dto.response.glamping;

import com.green.glampick.common.response.ResponseCode;
import com.green.glampick.common.response.ResponseMessage;
import com.green.glampick.dto.ResponseDto;
import com.green.glampick.dto.object.glamping.GlampingListItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Getter
@Setter
public class GetSearchGlampingListResponseDto extends ResponseDto {

    @Schema(example = "123", description = "검색된 글램핑 개수")
    private int searchCount;
    private List<GlampingListItem> glampingListItems;

    private GetSearchGlampingListResponseDto(int searchCount, List<GlampingListItem> glampingListItems) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.searchCount = searchCount;
        this.glampingListItems = glampingListItems;
    }

    public static ResponseEntity<GetSearchGlampingListResponseDto> success(int searchCount, List<GlampingListItem> glampingListItems) {
        GetSearchGlampingListResponseDto result = new GetSearchGlampingListResponseDto(searchCount, glampingListItems);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> isNull() {
        ResponseDto result = new ResponseDto(ResponseCode.RESULT_IS_NULL, ResponseMessage.RESULT_IS_NULL);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> validationFailed(String errorMsg) {
        ResponseDto result = new ResponseDto(ResponseCode.VALIDATION_FAILED, errorMsg);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }


}
