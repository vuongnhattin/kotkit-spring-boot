package com.example.kotkit.repository;

import com.example.kotkit.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Integer> {
    boolean existsByVideoIdAndUserId(int videoId, int userId);
    void deleteByVideoIdAndUserId(int videoId, int userId);
}
