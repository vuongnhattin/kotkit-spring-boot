package com.example.kotkit.dto.response;

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
public class UserProfileResponse {
    @JsonUnwrapped
    private UserResponse user;
    private Integer numberOfFriends;
    @Enumerated(EnumType.STRING)
    private FriendshipStatus friendStatus;
}
