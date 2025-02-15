package com.example.kotkit.controller;

import com.example.kotkit.dto.input.UpdateAvatarInput;
import com.example.kotkit.dto.input.UpdateUserInfoInput;
import com.example.kotkit.dto.response.ApiResponse;
import com.example.kotkit.dto.response.UserDetailsResponse;
import com.example.kotkit.entity.Users;
import com.example.kotkit.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "Search users by name or username")
    @GetMapping("users/search")
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<List<UserDetailsResponse>> getUsers(@RequestParam(value = "q") @Nullable String query, @RequestParam(value = "mock", defaultValue = "false") Boolean mock) {
        if (mock) return new ApiResponse<>(List.of());
        return new ApiResponse<>(userService.searchUsers(query), 400, "TOKEN_EXPIRED");
    }

    @Operation(summary = "Get current user")
    @GetMapping("me")
    public ApiResponse<Users> getMe() {
        return new ApiResponse<>(userService.getMe());
    }

    @Operation(summary = "Get user details")
    @GetMapping("users/{userId}/details")
    public ApiResponse<UserDetailsResponse> getUser(@PathVariable int userId) {
        return new ApiResponse<>(userService.getUserDetails(userId));
    }

    @Operation(summary = "Update info of current user")
    @PutMapping("me")
    public ApiResponse<Users> updateInfo(@RequestBody @Valid UpdateUserInfoInput input) {
        return new ApiResponse<>(userService.updateMe(input));
    }

    @Operation(summary = "Update avatar of current user")
    @PutMapping("me/avatar")
    public ApiResponse<Users> updateAvatar(@Valid @ModelAttribute UpdateAvatarInput input) {
        return new ApiResponse<>(userService.updateAvatar(input));
    }
}
