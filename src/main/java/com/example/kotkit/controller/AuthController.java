package com.example.kotkit.controller;

import com.example.kotkit.dto.request.LoginRequest;
import com.example.kotkit.dto.request.RegisterRequest;
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
    public Users register(@RequestBody @Valid RegisterRequest registerUserDto) {
        return authenticationService.register(registerUserDto);
    }

    @Operation(summary = "Login to the system")
    @PostMapping("login")
    public LoginResponse login(@RequestBody @Valid LoginRequest loginUserDto) {
        Users authenticatedUser = authenticationService.login(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        return LoginResponse.builder()
                .token(jwtToken)
                .expiresIn(jwtService.getExpirationTime())
                .build();
    }
}
