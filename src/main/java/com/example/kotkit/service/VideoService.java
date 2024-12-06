package com.example.kotkit.service;

import com.example.kotkit.dto.response.UserResponse;
import com.example.kotkit.dto.response.VideoResponse;
import com.example.kotkit.entity.Video;
import com.example.kotkit.entity.enums.VideoVisibility;
import com.example.kotkit.exception.AppException;
import com.example.kotkit.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class VideoService {
    private final VideoRepository videoRepository;
    private final UserService userService;
    private final FriendshipService friendshipService;

    public List<VideoResponse> getVideos(int creatorId, String visibility) {
        List<Video> videos;
        if (visibility.equals(VideoVisibility.PRIVATE.toString())) {
            int meId = userService.getCurrentUserId();

            if (meId == creatorId || friendshipService.isFriend(meId, creatorId)) {
                videos = videoRepository.getPrivateVideos(creatorId);
            } else {
                throw new AppException(403, "IS_NOT_FRIEND");
            }

        } else {
            videos = videoRepository.getPublicVideos(creatorId);
        }

        return videos.stream()
                .map(video -> {
                    UserResponse user = userService.getUserResponse(video.getCreatorId());
                    return new VideoResponse(video, user);
                })
                .toList();
    }
}
