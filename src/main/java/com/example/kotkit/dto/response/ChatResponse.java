package com.example.kotkit.dto.response;

import com.example.kotkit.entity.Chat;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatResponse {
    @JsonUnwrapped
    private Chat chat;
    String status;
}
