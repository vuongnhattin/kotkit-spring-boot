package com.example.kotkit.service;

import com.example.kotkit.dto.input.UpdateAvatarInput;
import com.example.kotkit.dto.input.UpdateUserInfoInput;
import com.example.kotkit.dto.response.UserDetailsResponse;
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
    private final MinioService minioService;

    public Users findByUsername(String username) {
        return userRepository.findByEmail(username).orElseThrow(() -> new AppException(404, USER_NOT_FOUND));
    }

    public Users findUserById(int userId) {
        return userRepository.findById(userId).orElseThrow(() -> new AppException(404, USER_NOT_FOUND));
    }

    public Users createUser(Users user) {
        return userRepository.save(user);
    }

    public Users getMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Users) authentication.getPrincipal();
    }

    public Integer getMeId() {
        return getMe().getUserId();
    }

    public Users updateMe(UpdateUserInfoInput input) {
        int meId = getMeId();
        Users user = getUserById(meId);

        mapper.map(input, user);
        return userRepository.save(user);
    }

    public boolean existsByEmail(String username) {
        return userRepository.existsByEmail(username);
    }

    public Users getUserById(int userId) {
        return userRepository.findById(userId).orElseThrow(() -> new AppException(404, USER_NOT_FOUND));
    }

    public List<UserDetailsResponse> searchUsers(String query) {
        int meId = getMeId();

        if (query == null || query.isEmpty()) {
            return userRepository.getAllUsers(meId);
        }

        return userRepository.searchUsers(query, meId);
    }

    public UserDetailsResponse getUserDetails(int userId) {
        return userRepository.getUserDetailsById(userId, getMeId());
    }

    public void increaseNumberOfFriends(int userId) {
        Users user = getUserById(userId);
        user.setNumberOfFriends(user.getNumberOfFriends() + 1);
        userRepository.save(user);
    }

    public void decreaseNumberOfFriends(int userId) {
        Users user = getUserById(userId);
        user.setNumberOfFriends(user.getNumberOfFriends() - 1);
        userRepository.save(user);
    }

    public Users updateAvatar(UpdateAvatarInput input) {
        if (input.getAvatar().isEmpty())
            throw new AppException(400, "AVATAR_EMPTY");
        Users user = getMe();
        user.setAvatar(minioService.upload(input.getAvatar()));
        return userRepository.save(user);
    }
}
