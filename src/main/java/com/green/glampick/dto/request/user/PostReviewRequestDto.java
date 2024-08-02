package com.green.glampick.dto.request.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.glampick.entity.ReservationCompleteEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class PostReviewRequestDto {

    @JsonIgnore private long userId;
    @JsonIgnore private long reviewId;
    @JsonIgnore private long glampId;
    private ReservationCompleteEntity reservationId;
    private String reviewContent;
    private int reviewStarPoint;
    @JsonIgnore private List<MultipartFile> ReviewImageFiles = new ArrayList<>();


}
