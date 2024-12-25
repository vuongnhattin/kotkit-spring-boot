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
    public Users register(@RequestBody @Valid RegisterInput registerUserDto) {
        return authenticationService.register(registerUserDto);
    }

    // eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsb25nQGVtYWlsIiwiaWF0IjoxNzM1MDkwNzEwLCJleHAiOjE3NDUwOTA3MTB9.jGaPOBKUqt3oQNj7CftHU8uAQhi6kLg7674mM_osuL0
    // eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0YW5AZW1haWwiLCJpYXQiOjE3MzUwOTA4MTIsImV4cCI6MTc0NTA5MDgxMn0.MC92DsgZYElE42ZXCTvlnxTxGNDWqHZCyKqKdSYz-7s
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
