package com.example.kotkit.service;

import com.example.kotkit.dto.input.VideoInput;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class VideoService {
    public static final String VIDEO_NOT_FOUND = "VIDEO_NOT_FOUND";
    private final VideoRepository videoRepository;
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final MinioClient minioClient;
    private final MinioService minioService;

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

    public void increaseNumberOfComments(Integer videoId, Integer quantity){
        if(quantity < 0) return;
        var video = videoRepository.findById(videoId)
                .orElseThrow(() -> new AppException(404, VIDEO_NOT_FOUND));
        video.setNumberOfComments(video.getNumberOfComments() + quantity);
        video = videoRepository.save(video);
    }
    public void decreaseNumberOfComments(Integer videoId, Integer quantity){
        if(quantity < 0) return;
        var video = videoRepository.findById(videoId)
                .orElseThrow(() -> new AppException(404, VIDEO_NOT_FOUND));
        if(video.getNumberOfComments() >= quantity)
            video.setNumberOfComments(video.getNumberOfComments() - quantity);
        else
            return;
        video = videoRepository.save(video);
    }

    public VideoResponse uploadVideo(VideoInput videoInput) {
        Video video = new Video();

        video.setCreatorId(userService.getMeId());
        video.setTitle(videoInput.getTitle());
        video.setMode(videoInput.getMode());
        video.setVideoUrl(minioService.upload(videoInput.getVideo()));
        video.setThumbnail(minioService.upload(videoInput.getThumbnail()));

        videoRepository.save(video);
        Users user = userService.getMe();

        return new VideoResponse(video, user);
    }

    public List<VideoResponse> getAllPublicVideos() {
        return videoRepository.getAllPublicVideos();
    }

    public List<VideoResponse> getAllPrivateVideos() {
        return videoRepository.getAllPrivateVideos();
    }

    public List<VideoResponse> getAllVideos() {
        return videoRepository.getAllVideos();
    }

    public Resource getVideoResource(Integer videoId) {
        Video video = findVideoById(videoId);
        try {
            GetObjectResponse response = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(video.getVideoUrl())
                            .build()
            );

            return new InputStreamResource(response);
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to get video", e);
        }
    }

    public VideoResponse increaseNumberOfLikes(Integer videoId) {
        Video video = findVideoById(videoId);
        Users creator = userService.getUserById(video.getCreatorId());
        video.setNumberOfLikes(video.getNumberOfLikes() + 1);
        video = videoRepository.save(video);
        return new VideoResponse(video, creator);
    }

    public VideoResponse decreaseNumberOfLikes(Integer videoId) {
        Video video = findVideoById(videoId);
        Users creator = userService.getUserById(video.getCreatorId());
        if (video.getNumberOfLikes() == 0) {
            throw new AppException(403, "IS_NOT_LIKE");
        }
        else {
            video.setNumberOfLikes(video.getNumberOfLikes() - 1);
            video = videoRepository.save(video);
        }
        return new VideoResponse(video, creator);
    }

    public List<VideoResponse> searchVideos(String query) {
        return videoRepository.searchVideos(query);
    }
}
