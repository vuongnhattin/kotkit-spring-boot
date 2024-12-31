package com.example.kotkit.repository;

import com.example.kotkit.dto.response.VideoResponse;
import com.example.kotkit.entity.Video;
import com.example.kotkit.entity.enums.VideoMode;
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

    @Query("""
            select new com.example.kotkit.dto.response.VideoResponse(
                v, u
            )
            from Video v, Users u
            where v.creatorId = u.userId
            and u.userId = :creatorId
            and v.mode = :mode
            """)
    List<VideoResponse> getVideosOfUser(@Param("creatorId") int creatorId, @Param("mode") VideoMode mode);

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

    @Query("""
        select distinct new com.example.kotkit.dto.response.VideoResponse(
            v, u
        )
        from Video v, Users u
        where (v.mode = 'FRIEND'
        or v.mode = 'PUBLIC')
        and u.userId = v.creatorId
        and v.creatorId in (
            select f.user2Id from Friendship f
            where f.user1Id = :userId
            and f.status = 'FRIEND'
        )
""")
    List<VideoResponse> getVideosOfFriends(@Param("userId") int userId);

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
    List<VideoResponse> getLikedVideosOfUser(int userId);

    @Query("""
        select new com.example.kotkit.dto.response.VideoResponse(
            v,
            u
        )
        from Video v
        join SavedVideo s on s.videoId = v.videoId
        join Users u on u.userId = s.userId
        where s.userId = :userId
        """)
    List<VideoResponse> getSavedVideosOfUser(int userId);
}
