package com.example.kotkit.dto.response;

import com.example.kotkit.entity.Chat;
import com.example.kotkit.entity.Comment;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {
    @JsonUnwrapped
    private Comment comment;
    String status;
}
