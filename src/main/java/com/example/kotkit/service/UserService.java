package com.example.kotkit.service;

import com.example.kotkit.dto.input.UpdateUserInput;
import com.example.kotkit.dto.response.UserResponse;
import com.example.kotkit.entity.Users;
import com.example.kotkit.exception.AppException;
import com.example.kotkit.repository.FriendshipRepository;
import com.example.kotkit.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    public static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;
    private final ModelMapper mapper;

    public Users findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new AppException(404, USER_NOT_FOUND));
    }

    public Users createUser(Users user) {
        return userRepository.save(user);
    }

    public Users getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Users) authentication.getPrincipal();
    }

    public Users updateUser(String username, UpdateUserInput input) {
        Users user = findByUsername(username);

        mapper.map(input, user);

        return userRepository.save(user);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public Users findByUserId(int userId) {
        return userRepository.findById(userId).orElseThrow(() -> new AppException(404, USER_NOT_FOUND));
    }

    public List<Users> searchUsers(String query) {
        if (query == null) {
            return userRepository.findAll();
        }
        return userRepository.searchUsers(query);
    }

    public UserResponse getUser(int userId) {
        Users users = findByUserId(userId);

        return mapper.map(users, UserResponse.class);
    }
}
