package com.example.kotkit.controller;

import com.example.kotkit.dto.response.DataResponse;
import com.example.kotkit.dto.response.UserDetailsResponse;
import com.example.kotkit.service.FriendshipService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FriendController {
    private final FriendshipService friendshipService;

    @Operation(summary = "Get all friends of a user")
    @GetMapping("users/{userId}/friends")
    public DataResponse<List<UserDetailsResponse>> getFriendsOfFriend(@PathVariable int userId) {
        return new DataResponse<>(friendshipService.getFriendsOfUser(userId));
    }

    @Operation(summary = "Send friend request to a user")
    @PutMapping("send-friend-request")
    public DataResponse sendFriendRequest(@RequestParam int userId) {
        friendshipService.sendFriendRequest(userId);
        return new DataResponse();
    }

    @Operation(summary = "Accept friend request from a user")
    @PutMapping("accept-friend-request")
    public DataResponse acceptFriendRequest(@RequestParam int userId) {
        friendshipService.acceptFriendRequest(userId);
        return new DataResponse();
    }

    @Operation(summary = "Reject friend request from a user")
    @PutMapping("reject-friend-request")
    public DataResponse rejectFriendRequest(@RequestParam int userId) {
        friendshipService.rejectFriendRequest(userId);
        return new DataResponse();
    }

    @Operation(summary = "Unfriend a user")
    @PutMapping("unfriend")
    public DataResponse unfriend(@RequestParam int userId) {
        friendshipService.unfriend(userId);
        return new DataResponse();
    }

    @Operation(summary = "Take back friend request to a user")
    @PutMapping("take-back-friend-request")
    public DataResponse takeBackFriendRequest(@RequestParam int userId) {
        friendshipService.takeBackFriendRequest(userId);
        return new DataResponse();
    }


}
