package com.example.kotkit.service;

import com.example.kotkit.dto.input.VideoInput;
import com.example.kotkit.dto.response.VideoResponse;
import com.example.kotkit.entity.Users;
import com.example.kotkit.entity.Video;
import com.example.kotkit.entity.Video;
import com.example.kotkit.entity.enums.VideoMode;
import com.example.kotkit.exception.AppException;
import com.example.kotkit.exception.ErrorCode;
import com.example.kotkit.repository.VideoRepository;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class VideoService {
    private final VideoRepository videoRepository;
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public Video findVideoById(Integer videoId) {
        return videoRepository.findById(videoId).orElseThrow(() -> new AppException(404, VIDEO_NOT_FOUND));
    }
    private final MinioService minioService;

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
    public void increaseNumberOfComments(Integer videoId, Integer quantity){
        if(quantity < 0) return;
        var video = videoRepository.findById(videoId)
                .orElseThrow(() -> new AppException(ErrorCode.VIDEO_NOT_FOUND.getStatus(), ErrorCode.VIDEO_NOT_FOUND.getCode()));
        video.setNumberOfComments(video.getNumberOfComments() + quantity);
        video = videoRepository.save(video);
    }
    public void decreaseNumberOfComments(Integer videoId, Integer quantity){
        if(quantity < 0) return;
        var video = videoRepository.findById(videoId)
                .orElseThrow(() -> new AppException(ErrorCode.VIDEO_NOT_FOUND.getStatus(), ErrorCode.VIDEO_NOT_FOUND.getCode()));
        if(video.getNumberOfComments() >= quantity)
            video.setNumberOfComments(video.getNumberOfComments() - quantity);
        else
            return;
        video = videoRepository.save(video);
    }

    public VideoResponse uploadVideo(VideoInput videoInput) {
        if (videoInput.getFile().isEmpty()) {
            throw new AppException(400, "Invalid file");
        }

        Video video = new Video();

        video.setCreatorId(userService.getMeId());
        video.setTitle(videoInput.getTitle());
        video.setMode(videoInput.getMode());
        video.setVideoUrl(minioService.upload(videoInput.getFile()));

        videoRepository.save(video);
        Users user = userService.getMe();

        return new VideoResponse(video, user);
    }

    public List<VideoResponse> getAllVideos() {
        List<VideoResponse> videos;
        videos = videoRepository.getAllVideos();
        return videos;
    }

    public Resource getVideoResource(Integer videoId) {
        Video video = findVideoById(videoId);
        try {
            GetObjectResponse response = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(video.getMinioObjectName())
                            .build()
            );

            return new InputStreamResource(response);
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to get video", e);
        }
    }
}
