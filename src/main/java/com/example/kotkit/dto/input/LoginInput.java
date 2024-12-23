package com.example.kotkit.dto.input;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginInput {
    @NotBlank(message = "EMAIL_REQUIRED")
    private String email;
    @NotBlank(message = "PASSWORD_REQUIRED")
    private String password;
}
