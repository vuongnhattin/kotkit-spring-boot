package com.example.kotkit.repository;

import com.example.kotkit.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Integer> {

    @Query("""
            select v from Video v
            where v.creatorId = :creatorId and v.visibility = 'PRIVATE'
            """)
    List<Video> getPrivateVideos(@Param("creatorId") int creatorId);

    @Query("""
            select v from Video v
            where v.creatorId = :creatorId and v.visibility = 'PUBLIC'
            """)
    List<Video> getPublicVideos(@Param("creatorId") int creatorId);
}
