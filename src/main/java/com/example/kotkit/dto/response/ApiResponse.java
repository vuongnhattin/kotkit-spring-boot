package com.example.kotkit.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T> {
    T data = null;
    @Builder.Default
    Integer status = 200;
    @Builder.Default
    String code = "SUCCESS";

    public ApiResponse(T data) {
        this.data = data;
    }
}
