package com.example.kotkit.controller;

import com.example.kotkit.dto.input.VideoInput;
import com.example.kotkit.dto.response.ApiResponse;
import com.example.kotkit.dto.response.VideoResponse;
import com.example.kotkit.entity.Video;
import com.example.kotkit.service.MinioService;
import com.example.kotkit.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("videos")
@RestController
@RequiredArgsConstructor
public class VideoController {
    private final VideoService videoService;

    @Operation(summary = "Get list of videos of a user")
    @GetMapping("videos-of-user")
    public ApiResponse<List<VideoResponse>> getVideos(@RequestParam("userId") int userId, @RequestParam(defaultValue = "public") String mode) {
        return new ApiResponse<>(videoService.getVideos(userId, mode));
    }

    @Operation(summary = "Upload video")
    @PostMapping("upload")
    public ApiResponse<VideoResponse> uploadVideo(@Valid VideoInput videoInput) {
        return new ApiResponse<>(videoService.uploadVideo(videoInput));
    }
}
