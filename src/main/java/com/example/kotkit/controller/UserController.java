package com.example.kotkit.controller;

import com.example.kotkit.dto.input.UpdateUserInfoInput;
import com.example.kotkit.dto.response.ApiResponse;
import com.example.kotkit.dto.response.UserDetailsResponse;
import com.example.kotkit.dto.response.UserInfoResponse;
import com.example.kotkit.service.FriendshipService;
import com.example.kotkit.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final FriendshipService friendshipService;

    @Operation(summary = "Search users by name or username")
    @GetMapping("users")
    public ApiResponse<List<UserDetailsResponse>> getUsers(@RequestParam(value = "q") @Nullable String query) {
        return new ApiResponse<>(userService.searchUsers(query));
    }

    @Operation(summary = "Get user details")
    @GetMapping("users/{userId}")
    public ApiResponse<UserDetailsResponse> getUser(@PathVariable int userId) {
        return new ApiResponse<>(userService.getUserDetails(userId));
    }

    @Operation(summary = "Update info of current user")
    @PutMapping("me/info")
    public ApiResponse<UserInfoResponse> updateInfo(@RequestBody @Valid UpdateUserInfoInput input) {
        return new ApiResponse<>(userService.updateMe(input));
    }

    @Operation(summary = "Get user details")
    @GetMapping("users/{userId}/profile")
    public ApiResponse<UserDetailsResponse> getUserDetails(@PathVariable int userId) {
        return new ApiResponse<>(userService.getUserDetails(userId));
    }
}
