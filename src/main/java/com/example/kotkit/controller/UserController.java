package com.example.kotkit.controller;

import com.example.kotkit.dto.response.DataResponse;
import com.example.kotkit.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {
    @Operation(summary = "Search users by name or username (all over tiktok)")
    @GetMapping("users")
    public DataResponse<List<UserResponse>> getUsers(@RequestParam(value = "q") String query) {
        return null;
    }

    @Operation(summary = "Search friends of a friend by name or username")
    @GetMapping("friends/{friendId}/friends")
    public DataResponse<List<UserResponse>> getFriendsOfFriend(@PathVariable int friendId, @RequestParam(value = "q") String query) {
        return null;
    }

    @Operation(summary = "Send friend request to a user")
    @PutMapping("send-friend-request")
    public DataResponse<UserResponse> sendFriendRequest(@RequestParam int userId) {
        return null;
    }

    @Operation(summary = "Accept friend request from a user")
    @PutMapping("accept-friend-request")
    public DataResponse<UserResponse> acceptFriendRequest(@RequestParam int userId) {
        return null;
    }

    @Operation(summary = "Reject friend request from a user")
    @PutMapping("reject-friend-request")
    public DataResponse<UserResponse> rejectFriendRequest(@RequestParam int userId) {
        return null;
    }

    @Operation(summary = "Unfriend a user")
    @PutMapping("unfriend")
    public DataResponse<UserResponse> unfriend(@RequestParam int userId) {
        return null;
    }
}
