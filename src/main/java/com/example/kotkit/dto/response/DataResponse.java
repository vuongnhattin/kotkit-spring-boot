package com.example.kotkit.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataResponse<T> {
    T data = null;
    Integer status = 200;
    String code = "SUCCESS";

    public DataResponse(T data) {
        this.data = data;
    }
}
