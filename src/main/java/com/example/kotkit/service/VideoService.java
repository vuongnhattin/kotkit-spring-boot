package com.example.kotkit.service;

import com.example.kotkit.dto.response.VideoResponse;
import com.example.kotkit.entity.enums.VideoMode;
import com.example.kotkit.exception.AppException;
import com.example.kotkit.exception.ErrorCode;
import com.example.kotkit.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class VideoService {
    private final VideoRepository videoRepository;
    private final UserService userService;
    private final FriendshipService friendshipService;

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
}
