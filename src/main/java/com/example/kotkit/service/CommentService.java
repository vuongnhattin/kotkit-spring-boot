package com.example.kotkit.service;

import com.example.kotkit.dto.input.CommentInput;
import com.example.kotkit.dto.response.CommentResponse;
import com.example.kotkit.entity.Comment;
import com.example.kotkit.entity.Users;
import com.example.kotkit.exception.AppException;
import com.example.kotkit.exception.ErrorCode;
import com.example.kotkit.repository.CommentRepository;
import com.example.kotkit.repository.VideoRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CommentService {
    CommentRepository commentRepository;
    VideoRepository videoRepository;
    VideoService videoService;
    ModelMapper modelMapper;
    public CommentResponse create(CommentInput input, Integer videoId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = (Users) authentication.getPrincipal();
        Comment comment = Comment.builder()
                .authorId(user.getUserId())
                .comment(input.getComment())
                .videoId(videoId)
                .build();
        comment = commentRepository.save(comment);
        videoService.increaseNumberOfComments(videoId, 1);
        CommentResponse commentResponse = CommentResponse.builder().build();
        modelMapper.map(comment, commentResponse);
        commentResponse.setAvatar(user.getAvatar());
        commentResponse.setFullName(user.getFullName());
        return commentResponse;
    }
    public List<CommentResponse> getAllInVideo(Integer videoId){
        return commentRepository.findAllByVideoId(videoId);
    }
    public void delete(Integer videoId, Integer commentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var user = (Users) authentication.getPrincipal();
        System.out.println(user.getUserId() + " delete " + commentId);
        commentRepository.deleteByCommentIdAndAuthorId(commentId, user.getUserId());
        videoService.decreaseNumberOfComments(videoId, 1);
    }
}
