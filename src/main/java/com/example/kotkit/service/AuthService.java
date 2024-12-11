package com.example.kotkit.service;

import com.example.kotkit.dto.input.LoginInput;
import com.example.kotkit.dto.input.RegisterInput;
import com.example.kotkit.dto.response.UserInfoResponse;
import com.example.kotkit.entity.Users;
import com.example.kotkit.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final ModelMapper mapper;

    public UserInfoResponse register(RegisterInput input) {
        if (userService.existsByUsername(input.getUsername())) {
            throw new AppException(400, "USERNAME_DUPLICATED");
        }

        Users user = mapper.map(input, Users.class);

        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setRoles("ROLE_USER");

        userService.createUser(user);

        return mapper.map(user, UserInfoResponse.class);
    }

    public Users login(LoginInput input) {
        if (!userService.existsByUsername(input.getUsername())) {
            throw new AppException(400, "USERNAME_NOT_FOUND");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.getUsername(),
                            input.getPassword()
                    )
            );

            return userService.findByUsername(input.getUsername());
        } catch (Exception e) {
            throw new AppException(400, "WRONG_PASSWORD");
        }
    }
}
