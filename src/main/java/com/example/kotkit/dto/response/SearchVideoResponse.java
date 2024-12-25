package com.example.kotkit.dto.response;

import com.example.kotkit.entity.enums.VideoMode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchVideoResponse {
    private Integer videoId;
    private String title;
    private String videoUrl;
    private String thumbnail;
    private Long numberOfViews = 0L;
    private Long numberOfLikes = 0L;
    private Long numberOfComments = 0L;
    private Integer creatorId;
    private VideoMode mode;
    private String createdAt;
    private String updatedAt;
    private String avatar;
    private String fullName;
}
