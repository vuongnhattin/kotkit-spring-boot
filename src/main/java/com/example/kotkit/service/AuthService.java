package com.example.kotkit.service;

import com.example.kotkit.dto.request.LoginRequest;
import com.example.kotkit.dto.request.RegisterRequest;
import com.example.kotkit.entity.Users;
import com.example.kotkit.exception.AppException;
import com.example.kotkit.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {
    public static final String USERNAME_NOT_FOUND = "Tên đăng nhập không tồn tại";
    public static final String USERNAME_DUPLICATED = "Tên đăng nhập đã tồn tại";
    public static final String WRONG_PASSWORD = "Sai mật khẩu";

    private final UserRepository userRepository;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final ModelMapper mapper;

    public Users register(RegisterRequest request) {
        Optional<Users> oldUser = userRepository.findByUsername(request.getUsername());
        if (oldUser.isPresent()) {
            throw new AppException(400, USERNAME_DUPLICATED);
        }

        Users user = mapper.map(request, Users.class);

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles("ROLE_USER");

        return userRepository.save(user);
    }

    public Users login(LoginRequest request) {
        if (!userService.existsByUsername(request.getUsername())) {
            throw new AppException(400, USERNAME_NOT_FOUND);
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            return userService.getUserByUsername(request.getUsername());
        } catch (Exception e) {
            throw new AppException(400, WRONG_PASSWORD);
        }
    }
}
