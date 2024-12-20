package com.example.kotkit.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {
    private Integer commentId;
    private Integer authorId;
    private Integer videoId;
    private String comment;
    private String createdAt;
    private String updatedAt;
}
