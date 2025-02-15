package com.example.kotkit.dto.response;

import com.example.kotkit.entity.Users;
import com.example.kotkit.entity.enums.FriendshipStatus;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetailsResponse {
    @JsonUnwrapped
    Users user;

    // Friend status with current user
    @Enumerated(EnumType.STRING)
    FriendshipStatus friendStatus;
}
