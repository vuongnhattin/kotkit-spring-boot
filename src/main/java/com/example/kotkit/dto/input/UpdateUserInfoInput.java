package com.example.kotkit.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserInfoInput {
    @NotBlank
    @Size(max = 50, message = "NAME_TOO_LONG")
    private String fullName;

    @NotNull
    private LocalDate birthday;
}
