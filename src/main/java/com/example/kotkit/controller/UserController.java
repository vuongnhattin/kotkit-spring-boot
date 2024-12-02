package com.example.kotkit.controller;

import com.example.kotkit.dto.input.UpdateUserInfoInput;
import com.example.kotkit.dto.response.DataResponse;
import com.example.kotkit.dto.response.UserProfileResponse;
import com.example.kotkit.dto.response.UserResponse;
import com.example.kotkit.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get user details")
    @GetMapping("users/{userId}")
    public DataResponse<UserResponse> getUser(@PathVariable int userId) {
        return new DataResponse<>(userService.getUserResponse(userId));
    }

    @Operation(summary = "Update info of current user")
    @PutMapping("me/info")
    public DataResponse<UserResponse> updateInfo(@RequestBody @Valid UpdateUserInfoInput input) {
        int meId = userService.getCurrentUserId();
        return new DataResponse<>(userService.updateUser(meId, input));
    }

    @Operation(summary = "Get user information on profile page")
    @GetMapping("users/{userId}/profile")
    public DataResponse<UserProfileResponse> getUserProfile(@PathVariable int userId) {
        return new DataResponse<>(userService.getUserProfile(userId));
    }
}
