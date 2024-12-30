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
            u
        )
        from Video v, Users u
        where lower(v.title) like lower(concat('%', :query, '%'))
        and v.creatorId = u.userId
    """)
    List<VideoResponse> searchVideos(@Param("query") String query);

    // Cai nay Tin viet chua can dung toi
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

    @Query("""
            select new com.example.kotkit.dto.response.VideoResponse(
                v, u
            )
            from Video v, Users u
            where v.creatorId = u.userId
            and v.mode = 'PUBLIC'
            """)
    List<VideoResponse> getAllPublicVideos();

    @Query("""
            select new com.example.kotkit.dto.response.VideoResponse(
                v, u
            )
            from Video v, Users u
            where v.creatorId = u.userId
            and v.mode = 'PRIVATE'
            """)
    List<VideoResponse> getAllPrivateVideos();

    // Sau nay can sua
    @Query("""
            select new com.example.kotkit.dto.response.VideoResponse(
                v,
                u
            )
            from Video v, Users u
            where v.creatorId = u.userId
            """)
    // where v.creatorId = u.userId
    List<VideoResponse> getAllVideos();

    @Query("""
        select new com.example.kotkit.dto.response.VideoResponse(
            v,
            u
        )
        from Video v
        join Like l on l.videoId = v.videoId
        join Users u on u.userId = l.userId   
        where l.userId = :userId
        """)
    List<VideoResponse> getAllLikedVideos(int userId);
}
