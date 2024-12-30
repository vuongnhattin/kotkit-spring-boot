package com.example.kotkit.repository;

import com.example.kotkit.entity.SavedVideo;
import org.springframework.data.repository.CrudRepository;

public interface SavedVideoRepository extends CrudRepository<SavedVideo, Integer> {
    boolean existsByVideoIdAndUserId(int videoId, int userId);
    void deleteByVideoIdAndUserId(int videoId, int userId);
}
