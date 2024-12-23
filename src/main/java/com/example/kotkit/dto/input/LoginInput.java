package com.example.kotkit.dto.input;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginInput {
    private String email;
    private String password;
}
