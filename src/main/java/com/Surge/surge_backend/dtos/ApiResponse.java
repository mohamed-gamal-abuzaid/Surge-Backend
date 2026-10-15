package com.Surge.surge_backend.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse {

    private int status;
    private String message;

    public static ApiResponse success(String message) {
        return ApiResponse.builder()
                .status(200)
                .message(message)
                .build();
    }
}