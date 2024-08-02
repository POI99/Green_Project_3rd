package com.green.glampick.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {
    private String code;
    private String message;

}