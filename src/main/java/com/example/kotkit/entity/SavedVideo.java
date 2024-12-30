package com.example.kotkit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "saved_video")
public class SavedVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer savedVideoId;
    private Integer videoId;
    private Integer userId;
}
