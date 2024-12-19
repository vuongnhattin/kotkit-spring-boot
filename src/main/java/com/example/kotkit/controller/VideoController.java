package com.example.kotkit.controller;

import com.example.kotkit.dto.response.ApiResponse;
import com.example.kotkit.dto.response.VideoResponse;
import com.example.kotkit.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class VideoController {
    private final VideoService videoService;

    @Operation(summary = "Get list of videos of a user")
    @GetMapping("videos-of-user")
    public ApiResponse<List<VideoResponse>> getVideos(@RequestParam("userId") int userId, @RequestParam(defaultValue = "public") String mode) {
        return new ApiResponse<>(videoService.getVideos(userId, mode));
    }
}
