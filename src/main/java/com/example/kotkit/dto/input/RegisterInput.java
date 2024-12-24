package com.example.kotkit.dto.input;

import com.example.kotkit.entity.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterInput {
    @NotBlank(message = "REQUIRED")
    private String email;
    @NotBlank(message = "REQUIRED")
    private String password;
    @NotBlank(message = "REQUIRED")
    private String fullName;
    @NotNull(message = "REQUIRED")
    private LocalDate birthday;
    @NotNull(message = "REQUIRED")
    private Gender gender;
}
