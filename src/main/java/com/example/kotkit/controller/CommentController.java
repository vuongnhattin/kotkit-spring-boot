package com.example.kotkit.controller;

import com.example.kotkit.dto.input.CommentInput;
import com.example.kotkit.dto.response.ApiResponse;
import com.example.kotkit.dto.response.CommentResponse;
import com.example.kotkit.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    @PostMapping("/{videoId}")
    public ApiResponse<CommentResponse> create(
            @PathVariable("videoId") Integer videoId,
            @RequestBody @Valid CommentInput input
    ){
        return ApiResponse.<CommentResponse>builder()
                .data(commentService.create(input, videoId))
                .build();
    }
    @DeleteMapping("/{videoId}")
    public ApiResponse<Void> delete(
            @PathVariable("videoId") Integer videoId,
            @RequestParam(name = "commentId") Integer commentId
    ){
        commentService.delete(videoId, commentId);
        return ApiResponse.<Void>builder()
                .build();
    }

    @GetMapping("/{videoId}")
    public ApiResponse<List<CommentResponse>> getAllCommentInVideo(
            @PathVariable("videoId") Integer videoId
    ){
        return ApiResponse.<List<CommentResponse>>builder()
                .data(commentService.getAllInVideo(videoId))
                .build();
    }
}
