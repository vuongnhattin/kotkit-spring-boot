package com.example.kotkit.controller;

import com.example.kotkit.dto.response.DataResponse;
import com.example.kotkit.dto.response.VideoResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class VideoController {
    @Operation(summary = "Edit video including changing title or mode")
    @PutMapping("/api/videos/{video_id}")
    public DataResponse<Null> editVideo(@PathVariable int video_id, @RequestBody VideoResponse videoResponse) {
        return null;
    }

    @Operation(summary = "Comment video")
    @PostMapping("api/videos/{video_id}/comment")
    public DataResponse<Null> commentVideo(@PathVariable int video_id, @RequestBody VideoResponse videoResponse) {
        return null;
    }

}
