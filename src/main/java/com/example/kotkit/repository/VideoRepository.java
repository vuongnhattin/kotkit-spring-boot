package com.example.kotkit.repository;

import com.example.kotkit.dto.response.VideoResponse;
import com.example.kotkit.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Integer> {

    @Query("""
            select new com.example.kotkit.dto.response.VideoResponse(
            v,
            new com.example.kotkit.dto.response.UserInfoResponse(u.id, u.username, u.fullName, u.avatar, u.birthday)
            ) from Video v, Users u
            where v.creatorId = u.id
            and u.id = :creatorId
            and v.visibility = 'PRIVATE'
            """)
    List<VideoResponse> getPrivateVideos(@Param("creatorId") int creatorId);

    @Query("""
            select new com.example.kotkit.dto.response.VideoResponse(
            v,
            new com.example.kotkit.dto.response.UserInfoResponse(u.id, u.username, u.fullName, u.avatar, u.birthday)
            ) from Video v, Users u
            where v.creatorId = u.id
            and u.id = :creatorId
            and v.visibility = 'PUBLIC'
            """)
    List<VideoResponse> getPublicVideos(@Param("creatorId") int creatorId);
}