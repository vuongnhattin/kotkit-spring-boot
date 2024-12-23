package com.example.kotkit.entity;

import com.example.kotkit.entity.enums.VideoMode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
@Entity
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer videoId;
    private String title;
    private String videoUrl;
    private String thumbnail;
    private Long numberOfLikes = 0L;
    private Long numberOfComments = 0L;
    private Long numberOfViews = 0L;
    @JsonIgnore
    private Integer creatorId;
    @Enumerated(EnumType.STRING)
    private VideoMode mode;
    @CreationTimestamp
    private String createdAt;
    @UpdateTimestamp
    private String updatedAt;
}
