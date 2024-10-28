package com.example.kotkit.service;

import com.example.kotkit.dto.request.UpdateUserRequest;
import com.example.kotkit.entity.Users;
import com.example.kotkit.exception.AppException;
import com.example.kotkit.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    public static final String USER_NOT_FOUND = "Không tìm thấy user";

    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Users) authentication.getPrincipal();
    }

    public Users updateUser(String username, UpdateUserRequest request) {
        Users user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(404, USER_NOT_FOUND));

        user.setFullName(request.getFullName());

        return user;
    }

    public Users getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new AppException(404, USER_NOT_FOUND));
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
