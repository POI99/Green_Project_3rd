package com.green.glampick.dto.response.main;

import com.green.glampick.dto.ResponseDto;
import com.green.glampick.dto.object.main.MountainViewGlampingItem;
import com.green.glampick.dto.object.main.PopularGlampingItem;
import com.green.glampick.dto.object.main.PetFriendlyGlampingItem;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.green.glampick.common.GlobalConst.SUCCESS_CODE;
import static com.green.glampick.common.GlobalConst.SUCCESS_MESSAGE;

@Getter
@Setter
public class GetMainGlampingListResponseDto extends ResponseDto {

    private List<PopularGlampingItem> popular;
    private List<PetFriendlyGlampingItem> petFriendly;
    private List<MountainViewGlampingItem> mountainView;

    public GetMainGlampingListResponseDto(List<PopularGlampingItem> popular, List<PetFriendlyGlampingItem> petFriendly
                                , List<MountainViewGlampingItem> mountainView) {
        super(SUCCESS_CODE, SUCCESS_MESSAGE);
        this.popular = popular;
        this.petFriendly = petFriendly;
        this.mountainView = mountainView;
    }

    public static ResponseEntity<ResponseDto> success(List<PopularGlampingItem> popular, List<PetFriendlyGlampingItem> petFriendly
            , List<MountainViewGlampingItem> mountainView) {
        GetMainGlampingListResponseDto result = new GetMainGlampingListResponseDto(popular, petFriendly, mountainView);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
