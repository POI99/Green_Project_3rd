package com.green.glampick.controller;

import com.green.glampick.dto.request.book.GetBookPayRequestDto;
import com.green.glampick.dto.request.book.PostBookRequestDto;
import com.green.glampick.dto.response.book.GetBookPayResponseDto;
import com.green.glampick.dto.response.book.PostBookResponseDto;
import com.green.glampick.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book")
@Tag(name = "예약 컨트롤러")
public class BookController {
    private final BookService service;

    @PostMapping
    @Operation(summary = "글램핑 예약하기 (김수찬)", description = "<strong>글램핑 예약을 할 수 있습니다.</strong>",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "성공에 대한 반환 값 입니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PostBookResponseDto.class)
                    ))})
    public ResponseEntity<? super PostBookResponseDto> postBook(@RequestBody PostBookRequestDto dto) {
        return service.postBook(dto);
    }

    @GetMapping("reservation")
    @Operation(summary = "최종 결제가격 정보 (김수찬)", description = "<strong>최종 결제가격을 계산하여 응답합니다.</strong>",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "성공에 대한 반환 값 입니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GetBookPayResponseDto.class)
                    ))})
    public ResponseEntity<? super GetBookPayResponseDto> getReservationAmount (@ParameterObject @ModelAttribute GetBookPayRequestDto dto) {
        return service.getReservationAmount(dto);
    }

    // 강국 =================================================================================================================



}
