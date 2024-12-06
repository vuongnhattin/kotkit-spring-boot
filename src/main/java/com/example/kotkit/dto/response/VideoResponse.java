package com.example.kotkit.dto.response;

import com.example.kotkit.entity.Video;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideoResponse {
    @JsonUnwrapped
    private Video video;
    String status;
}
