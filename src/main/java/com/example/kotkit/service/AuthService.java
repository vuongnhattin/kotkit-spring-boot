package com.example.kotkit.service;

import com.example.kotkit.dto.input.LoginInput;
import com.example.kotkit.dto.input.RegisterInput;
import com.example.kotkit.entity.Users;
import com.example.kotkit.exception.AppException;
import com.example.kotkit.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper mapper;

    public void register(RegisterInput input) {
        if (userService.existsByEmail(input.getEmail())) {
            throw new AppException(400, "EMAIL_DUPLICATED");
        }

        Users user = mapper.map(input, Users.class);

        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setRoles("ROLE_USER");

        userService.createUser(user);
    }

    public Users login(LoginInput input) {
        if (!userService.existsByEmail(input.getEmail())) {
            throw new AppException(400, "EMAIL_NOT_FOUND");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.getEmail(),
                            input.getPassword()
                    )
            );

            return userService.findByUsername(input.getEmail());
        } catch (Exception e) {
            throw new AppException(400, "WRONG_PASSWORD");
        }
    }

    public void changePassword(String oldPassword, String newPassword) {
        Users user = userService.getMe();

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new AppException(400, "WRONG_PASSWORD");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
