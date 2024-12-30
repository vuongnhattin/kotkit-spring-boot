package com.example.kotkit.controller;

import com.example.kotkit.dto.input.ChangePasswordInput;
import com.example.kotkit.dto.input.LoginInput;
import com.example.kotkit.dto.input.RegisterInput;
import com.example.kotkit.dto.response.ApiResponse;
import com.example.kotkit.dto.response.LoginResponse;
import com.example.kotkit.entity.Users;
import com.example.kotkit.service.AuthService;
import com.example.kotkit.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("auth")
@RestController
@AllArgsConstructor
public class AuthController {
    private final JwtService jwtService;

    private final AuthService authService;

    @Operation(summary = "Register a new user")
    @PostMapping("register")
    public ApiResponse<Void> register(@RequestBody @Valid RegisterInput registerUserDto) {
        authService.register(registerUserDto);
        return new ApiResponse<>();
    }

    @Operation(summary = "Login to the system")
    @PostMapping("login")
    public ApiResponse<LoginResponse> login(@RequestBody @Valid LoginInput loginUserDto) {
        Users authenticatedUser = authService.login(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse response = LoginResponse.builder()
                .token(jwtToken)
                .expiresIn(jwtService.getExpirationTime())
                .build();

        return new ApiResponse<>(response);
    }

    @Operation(summary = "Change password of current user")
    @PutMapping("change-password")
    public ApiResponse<Void> changePassword(@RequestBody @Valid ChangePasswordInput input) {
        authService.changePassword(input.getOldPassword(), input.getNewPassword());
        return new ApiResponse<>();
    }
}
