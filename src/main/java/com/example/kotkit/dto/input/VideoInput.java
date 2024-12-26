package com.example.kotkit.dto.input;

import com.example.kotkit.entity.enums.VideoMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideoInput {
    @NotBlank(message = "TITLE_REQUIRED")
    private String title;
    @NotNull(message = "VIDEO_MODE_REQUIRED")
    private VideoMode mode;
    @NotNull(message = "THUMBNAIL_REQUIRED")
    private MultipartFile thumbnail;
    @NotNull(message = "VIDEO_REQUIRED")
    private MultipartFile video;
}
