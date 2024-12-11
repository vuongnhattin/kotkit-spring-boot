package com.example.kotkit.service;

import com.example.kotkit.dto.input.UpdateUserInfoInput;
import com.example.kotkit.dto.response.UserDetailsResponse;
import com.example.kotkit.dto.response.UserInfoResponse;
import com.example.kotkit.entity.Users;
import com.example.kotkit.exception.AppException;
import com.example.kotkit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    public static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public Users findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new AppException(404, USER_NOT_FOUND));
    }

    public Users createUser(Users user) {
        return userRepository.save(user);
    }

    public Users getMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Users) authentication.getPrincipal();
    }

    public int getMeId() {
        return getMe().getId();
    }

    public UserInfoResponse updateMe(UpdateUserInfoInput input) {
        int meId = getMeId();
        Users user = getUser(meId);

        mapper.map(input, user);
        userRepository.save(user);

        return mapper.map(user, UserInfoResponse.class);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public Users getUser(int userId) {
        return userRepository.findById(userId).orElseThrow(() -> new AppException(404, USER_NOT_FOUND));
    }

    public List<UserDetailsResponse> searchUsers(String query) {
        int meId = getMeId();

        if (query == null) {
            return userRepository.getAllUsers(meId);
        }

        return userRepository.searchUsers(query, meId);
    }

    public UserInfoResponse getUserInfo(int userId) {
        Users users = getUser(userId);

        return mapper.map(users, UserInfoResponse.class);
    }

    public UserDetailsResponse getUserDetails(int userId) {
        return userRepository.getUserDetailsById(userId, getMeId());
    }
}
