package com.example.kotkit.service;

import com.example.kotkit.dto.input.VideoInput;
import com.example.kotkit.dto.response.VideoResponse;
import com.example.kotkit.entity.Like;
import com.example.kotkit.entity.SavedVideo;
import com.example.kotkit.entity.Users;
import com.example.kotkit.entity.Video;
import com.example.kotkit.entity.enums.VideoMode;
import com.example.kotkit.exception.AppException;
import com.example.kotkit.repository.LikeRepository;
import com.example.kotkit.repository.SavedVideoRepository;
import com.example.kotkit.repository.VideoRepository;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import jakarta.transaction.Transactional;
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
    private final LikeRepository likeRepository;
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final MinioClient minioClient;
    private final MinioService minioService;
    private final SavedVideoRepository savedVideoRepository;

    @Value("${minio.bucket}")
    private String bucketName;

    public Video findVideoById(Integer videoId) {
        return videoRepository.findById(videoId).orElseThrow(() -> new AppException(404, VIDEO_NOT_FOUND));
    }

    public List<VideoResponse> getVideosOfUser(int creatorId, String mode) {
        if (mode.equals(VideoMode.FRIEND.name())) {
            if (creatorId != userService.getMeId() && !friendshipService.isFriend(creatorId, userService.getMeId())) {
                throw new AppException(400, "NOT_FRIEND");
            }
        }
        if (mode.equals(VideoMode.PRIVATE.name()) && creatorId != userService.getMeId()) {
            throw new AppException(400, "FORBIDDEN");
        }
        VideoMode videoMode = VideoMode.valueOf(mode);
        return videoRepository.getVideosOfUser(creatorId, videoMode);
    }

    public List<VideoResponse> getPrivateVideosOfMe() {
        return videoRepository.getVideosOfUser(userService.getMeId(), VideoMode.valueOf("PRIVATE"));
    }

    public List<VideoResponse> getVideosOfFriends() {
        return videoRepository.getVideosOfFriends(userService.getMeId());
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
        if (videoInput.getVideo().isEmpty())
            throw new AppException(400, "VIDEO_EMPTY");

        if (videoInput.getThumbnail().isEmpty())
            throw new AppException(400, "THUMBNAIL_EMPTY");

        Video video = new Video();

        video.setCreatorId(userService.getMeId());
        video.setTitle(videoInput.getTitle());
        video.setMode(videoInput.getMode());
        video.setVideoUrl(minioService.upload(videoInput.getVideo()));
        video.setThumbnail(minioService.upload(videoInput.getThumbnail()));

        videoRepository.save(video);
        Users user = userService.getMe();
        user.setNumberOfVideos(user.getNumberOfVideos() + 1);

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

    @Transactional
    public VideoResponse updateLikeVideoState(Integer videoId) {
        Video video = findVideoById(videoId);
        Users user = userService.getMe();
        if (likeRepository.existsByVideoIdAndUserId(video.getVideoId(), user.getUserId())) {
            video.setNumberOfLikes(video.getNumberOfLikes() - 1);
            likeRepository.deleteByVideoIdAndUserId(video.getVideoId(), user.getUserId());
        }
        else {
            video.setNumberOfLikes(video.getNumberOfLikes() + 1);
            Like like = new Like();
            like.setVideoId(video.getVideoId());
            like.setUserId(user.getUserId());
            likeRepository.save(like);
        }
        video = videoRepository.save(video);
        Users creator = userService.getUserById(video.getCreatorId());
        return new VideoResponse(video, creator);
    }

    @Transactional
    public VideoResponse updateSaveVideoState(Integer videoId) {
        Video video = findVideoById(videoId);
        Users user = userService.getMe();
        if (savedVideoRepository.existsByVideoIdAndUserId(video.getVideoId(), user.getUserId())) {
            savedVideoRepository.deleteByVideoIdAndUserId(video.getVideoId(), user.getUserId());
        }
        else {
            SavedVideo savedVideo = new SavedVideo();
            savedVideo.setVideoId(video.getVideoId());
            savedVideo.setUserId(user.getUserId());
            savedVideoRepository.save(savedVideo);
        }
        Users creator = userService.getUserById(video.getCreatorId());
        return new VideoResponse(video, creator);
    }

    public List<VideoResponse> getLikedVideosOfMe() {
        Users user = userService.getMe();
        return videoRepository.getLikedVideosOfUser(user.getUserId());
    }

    public List<VideoResponse> getSavedVideosOfMe() {
        Users user = userService.getMe();
        return videoRepository.getSavedVideosOfUser(user.getUserId());
    }

    public List<VideoResponse> searchVideos(String query) {
        return videoRepository.searchVideos(query);
    }
}
