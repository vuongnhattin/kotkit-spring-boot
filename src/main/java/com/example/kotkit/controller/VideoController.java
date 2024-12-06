package com.example.kotkit.controller;

import com.example.kotkit.dto.response.DataResponse;
import com.example.kotkit.dto.response.VideoResponse;
import com.example.kotkit.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class VideoController {
    @Operation(summary = "Edit video including changing title or mode")
    @PutMapping("/api/videos/{video_id}")
    public DataResponse<Null> editVideo(@PathVariable int video_id, @RequestBody VideoResponse videoResponse) {
        return null;
    }
    private final VideoService videoService;

    @Operation(summary = "Get list of videos of a user")
    @GetMapping("videos-of-user")
    public DataResponse<List<VideoResponse>> getVideos(@RequestParam("userId") int userId, @RequestParam(defaultValue = "public") String visibility) {
        return new DataResponse<>(videoService.getVideos(userId, visibility));
    }
    @Operation(summary = "Comment video")
    @PostMapping("api/videos/{video_id}/comment")
    public DataResponse<Null> commentVideo(@PathVariable int video_id, @RequestBody VideoResponse videoResponse) {
        return null;
    }

}
