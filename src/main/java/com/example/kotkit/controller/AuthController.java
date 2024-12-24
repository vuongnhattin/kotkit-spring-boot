package com.example.kotkit.controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("auth")
@RestController
@AllArgsConstructor
public class AuthController {
    private final JwtService jwtService;

    private final AuthService authenticationService;

    @Operation(summary = "Register a new user")
    @PostMapping("register")
    public ApiResponse<Void> register(@RequestBody @Valid RegisterInput registerUserDto) {
        authenticationService.register(registerUserDto);
        return new ApiResponse<>();
    }

    @Operation(summary = "Login to the system")
    @PostMapping("login")
    public ApiResponse<LoginResponse> login(@RequestBody @Valid LoginInput loginUserDto) {
        Users authenticatedUser = authenticationService.login(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse response = LoginResponse.builder()
                .token(jwtToken)
                .expiresIn(jwtService.getExpirationTime())
                .build();

        return new ApiResponse<>(response);
    }
}
