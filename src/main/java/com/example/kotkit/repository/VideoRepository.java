package com.example.kotkit.repository;

import com.example.kotkit.dto.response.VideoResponse;
import com.example.kotkit.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Integer> {
    @Query("""
            select new com.example.kotkit.dto.response.SearchVideoResponse(
                v.videoId,
                v.title,
                v.videoUrl,
                v.thumbnail,
                v.numberOfViews,
                v.numberOfLikes,
                v.numberOfComments,
                v.creatorId,
                v.mode,
                v.createdAt,
                v.updatedAt,
                u.avatar,
                u.fullName
            )
            from Video v, Users u
            where lower(v.title) like lower(concat('%', :query, '%'))
            and v.creatorId = u.userId
            """)
    List<VideoResponse> searchVideos(@Param("query") String query);

    @Query("""
            select new com.example.kotkit.dto.response.VideoResponse(
                v, u
            )
            from Video v, Users u
            where v.creatorId = u.userId
            and u.userId = :creatorId
            and v.mode = 'PRIVATE'
            """)
    List<VideoResponse> getPrivateVideos(@Param("creatorId") int creatorId);

    @Query("""
            select new com.example.kotkit.dto.response.VideoResponse(
                v, u
            )
            from Video v, Users u
            where v.creatorId = u.userId
            and u.userId = :creatorId
            and v.mode = 'PUBLIC'
            """)
    List<VideoResponse> getPublicVideos(@Param("creatorId") int creatorId);

    // Sau nay can sua
    @Query("""
            select new com.example.kotkit.dto.response.VideoResponse(
                v,
                u
            )
            from Video v, Users u
            """)
    // where v.creatorId = u.userId
    List<VideoResponse> getAllVideos();
}
