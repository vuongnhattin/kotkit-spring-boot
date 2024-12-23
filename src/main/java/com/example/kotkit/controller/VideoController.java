package com.example.kotkit.controller;

import com.example.kotkit.dto.input.VideoInput;
import com.example.kotkit.dto.response.ApiResponse;
import com.example.kotkit.dto.response.VideoDataResponse;
import com.example.kotkit.dto.response.VideoResponse;
import com.example.kotkit.entity.Video;
import com.example.kotkit.service.MinioService;
import com.example.kotkit.service.VideoService;
import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping("videos")
@RestController
@RequiredArgsConstructor
public class VideoController {
    private final VideoService videoService;
    private final MinioService minioService;

    @Operation(summary = "Get list of videos of a user")
    @GetMapping("videos-of-user")
    public ApiResponse<List<VideoResponse>> getVideos(@RequestParam("userId") int userId, @RequestParam(defaultValue = "public") String mode) {
        return new ApiResponse<>(videoService.getVideos(userId, mode));
    }

    @Operation(summary = "Upload video")
    @PostMapping("/")
    public ApiResponse<VideoResponse> uploadVideo(@Valid VideoInput videoInput) {
        return new ApiResponse<>(videoService.uploadVideo(videoInput));
    }

    // Cai nay khong dung nua de do di nhe
    @Operation(summary = "Get one video")
    @GetMapping("/{videoId}")
    public ApiResponse<VideoDataResponse> getVideo(@PathVariable Integer videoId) {
        return new ApiResponse<>(minioService.getVideo(videoId));
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
}
