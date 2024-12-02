package com.example.kotkit.controller;

import com.example.kotkit.dto.response.DataResponse;
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
    public DataResponse<List<VideoResponse>> getVideos(@RequestParam("userId") int userId, @RequestParam(defaultValue = "public") String visibility) {
        return new DataResponse<>(videoService.getVideos(userId, visibility));
    }
}
