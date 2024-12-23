package com.example.kotkit.service;

import com.example.kotkit.dto.response.VideoDataResponse;
import com.example.kotkit.dto.response.VideoResponse;
import com.example.kotkit.entity.Users;
import com.example.kotkit.entity.Video;
import com.example.kotkit.entity.enums.VideoMode;
import com.example.kotkit.exception.AppException;
import com.example.kotkit.repository.VideoRepository;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class VideoService {
    public static final String VIDEO_NOT_FOUND = "VIDEO_NOT_FOUND";
    private final VideoRepository videoRepository;
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

    public Video findVideoById(Integer videoId) {
        return videoRepository.findById(videoId).orElseThrow(() -> new AppException(404, VIDEO_NOT_FOUND));
    }

    public List<VideoResponse> getVideos(int creatorId, String mode) {
        List<VideoResponse> videos;
        if (mode.equals(VideoMode.PRIVATE.toString())) {
            int meId = userService.getMeId();

            if (meId == creatorId || friendshipService.isFriend(meId, creatorId)) {
                videos = videoRepository.getPrivateVideos(creatorId);
            } else {
                throw new AppException(403, "IS_NOT_FRIEND");
            }

        } else {
            videos = videoRepository.getPublicVideos(creatorId);
        }

        return videos;
    }

    public VideoResponse uploadVideo(Video video) {
        videoRepository.save(video);
        Users user = userService.getUserById(video.getCreatorId());
        return new VideoResponse(video, user);
    }

    public List<VideoResponse> getAllVideos() {
        return videoRepository.getAllVideos();
    }

}
