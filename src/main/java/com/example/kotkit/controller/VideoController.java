package com.example.kotkit.controller;

import com.example.kotkit.dto.input.UpdateVideoInfoInput;
import com.example.kotkit.dto.input.VideoInput;
import com.example.kotkit.dto.response.ApiResponse;
import com.example.kotkit.dto.response.VideoDataResponse;
import com.example.kotkit.dto.response.VideoResponse;
import com.example.kotkit.service.MinioService;
import com.example.kotkit.service.VideoService;
import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequestMapping("videos")
@RestController
@RequiredArgsConstructor
public class VideoController {
    private final VideoService videoService;
    private final MinioService minioService;

    @Operation(summary = "Get list of videos of a user (public and friend)")
    @GetMapping("videos-of-user")
    public ApiResponse<List<VideoResponse>> getVideos(@RequestParam("userId") int userId, @RequestParam(defaultValue = "PUBLIC") String mode) {
        return new ApiResponse<>(videoService.getVideosOfUser(userId, mode));
    }

    @Operation(summary = "Get all private videos of current user")
    @GetMapping("private-videos-of-me")
    public ApiResponse<List<VideoResponse>> getPrivateVideosOfMe() {
        return new ApiResponse<>(videoService.getPrivateVideosOfMe());
    }

    @Operation(summary = "Upload video")
    @PostMapping("upload")
    public ApiResponse<VideoResponse> uploadVideo(@Valid @ModelAttribute VideoInput videoInput) {
        return new ApiResponse<>(videoService.uploadVideo(videoInput));
    }

    // Cai nay khong dung nua de do di nhe - Dung de tai mot video tu server ve client
    @Operation(summary = "Get one video")
    @GetMapping("/{videoId}")
    public ApiResponse<VideoDataResponse> getVideo(@PathVariable Integer videoId) {
        return new ApiResponse<>(minioService.getVideo(videoId));
    }

    @Operation(summary = "Get list of all public videos")
    @GetMapping("public-videos")
    public ApiResponse<List<VideoResponse>> getPublicVideos() {
        return new ApiResponse<>(videoService.getAllPublicVideos());
    }

    @Operation(summary = "Get list of all private videos")
    @GetMapping("private-videos")
    public ApiResponse<List<VideoResponse>> getPrivateVideos() {
        return new ApiResponse<>(videoService.getAllPrivateVideos());
    }

    @Operation(summary = "Get list of all public and friend videos of friends")
    @GetMapping("videos-of-friends")
    public ApiResponse<List<VideoResponse>> getVideosOfFriends() {
        return new ApiResponse<>(videoService.getVideosOfFriends());
    }

    @Operation(summary = "Get list of all videos - for testing")
    @GetMapping("all-videos")
    public ApiResponse<List<VideoResponse>> getAllVideos() {
        return new ApiResponse<>(videoService.getAllVideos());
    }

    private ResourceRegion resourceRegion(Resource video, String httpRangeList) throws IOException {
        long contentLength = video.contentLength();

        if (StringUtils.isEmpty(httpRangeList)) {
            long rangeLength = Math.min(1024 * 1024, contentLength);
            return new ResourceRegion(video, 0, rangeLength);
        }

        String[] ranges = httpRangeList.substring("bytes=".length()).split("-");
        long start = Long.parseLong(ranges[0]);
        long end = ranges.length > 1 ? Long.parseLong(ranges[1])
                : Math.min(start + 1024 * 1024, contentLength - 1);

        return new ResourceRegion(video, start, end - start + 1);
    }
    @Operation(summary = "Search Video")
    @GetMapping("/search")
    public ApiResponse<List<VideoResponse>> searchVideos(
            @RequestParam(value = "q") @Nullable String query
    ) {
        return new ApiResponse<>(videoService.searchVideos(query));
    }

    @Operation(summary = "Update like video state")
    @PostMapping("{videoId}/like")
    public ApiResponse<VideoResponse> updateLikeVideoState(@PathVariable Integer videoId) {
        return new ApiResponse<>(videoService.updateLikeVideoState(videoId));
    }

    @Operation(summary = "Update save video state")
    @PostMapping("{videoId}/save")
    public ApiResponse<VideoResponse> updateSaveVideoState(@PathVariable Integer videoId) {
        return new ApiResponse<>(videoService.updateSaveVideoState(videoId));
    }

    @Operation(summary = "Get list of all liked videos of current user")
    @GetMapping("liked")
    public ApiResponse<List<VideoResponse>> getAllLikedVideos() {
        return new ApiResponse<>(videoService.getLikedVideosOfMe());
    }

    @Operation(summary = "Get list of all saved videos of current user")
    @GetMapping("saved-videos-of-me")
    public ApiResponse<List<VideoResponse>> getAllSavedVideos() {
        return new ApiResponse<>(videoService.getSavedVideosOfMe());
    }

    @Operation(summary = "Update video (title, mode, delete)")
    @PostMapping("{videoId}/update")
    public ApiResponse<Void> UpdateVideoInfo(@PathVariable Integer videoId, @RequestBody UpdateVideoInfoInput updateVideoInfoInput) {
        return new ApiResponse<>(videoService.updateVideoInfo(
                videoId,
                updateVideoInfoInput
        ));
    }
}
