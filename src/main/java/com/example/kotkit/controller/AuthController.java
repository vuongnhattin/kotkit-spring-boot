package com.example.kotkit.controller;

import com.example.kotkit.dto.input.LoginInput;
import com.example.kotkit.dto.input.RegisterInput;
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

    // eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuZ3VAZW1haWwuY29tIiwiaWF0IjoxNzM0NzQyOTg0LCJleHAiOjE3NDQ3NDI5ODR9.0KVaEmxH7AtRS7APLf-2_NX5lYmqB916DsFJToRgD4g
    @Operation(summary = "Login to the system")
    @PostMapping("login")
    public LoginResponse login(@RequestBody @Valid LoginInput loginUserDto) {
        Users authenticatedUser = authenticationService.login(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        return LoginResponse.builder()
                .token(jwtToken)
                .expiresIn(jwtService.getExpirationTime())
                .build();
    }
}
