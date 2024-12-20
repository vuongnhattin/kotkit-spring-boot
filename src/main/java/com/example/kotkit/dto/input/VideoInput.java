package com.example.kotkit.dto.input;

import com.example.kotkit.entity.Users;
import com.example.kotkit.entity.Video;
import com.example.kotkit.entity.enums.VideoMode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideoInput {
    @NotBlank
    private String title;
    @NotBlank
    private Integer creatorId;
    @NotBlank
    private VideoMode mode;
}
