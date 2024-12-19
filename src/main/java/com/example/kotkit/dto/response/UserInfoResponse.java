package com.example.kotkit.dto.response;

import com.example.kotkit.entity.enums.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoResponse {
    private Integer userId;
    private String email;
    private String fullName;
    private String avatar;
    private LocalDate birthday;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private Boolean isBlocked;
    private Boolean isVerified;
}
