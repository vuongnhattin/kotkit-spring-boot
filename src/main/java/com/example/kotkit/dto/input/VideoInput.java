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
    @NotBlank
    private String title;
    private VideoMode mode;
    @NotNull
    private MultipartFile file;
}
