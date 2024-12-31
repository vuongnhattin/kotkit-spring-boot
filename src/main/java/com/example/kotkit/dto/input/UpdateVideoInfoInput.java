package com.example.kotkit.dto.input;

import com.example.kotkit.entity.enums.VideoMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateVideoInfoInput {
    @NotBlank(message = "TITLE_REQUIRED")
    private String title;
    @NotNull(message = "VIDEO_MODE_REQUIRED")
    private VideoMode mode;
}
