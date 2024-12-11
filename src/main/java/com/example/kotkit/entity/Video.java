package com.example.kotkit.entity;

import com.example.kotkit.entity.enums.VideoVisibility;
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
    private Integer id;
    private String title;
    private String videoUrl;
    private String thumbnail;
    private Integer numberOfLikes;
    private Integer numberOfComments;
    private Integer numberOfViews;
    @JsonIgnore
    private Integer creatorId;
    @Enumerated(EnumType.STRING)
    private VideoVisibility visibility;
    @CreationTimestamp
    private String createdAt;
    @UpdateTimestamp
    private String updatedAt;
}
