package com.example.kotkit.service;

import com.example.kotkit.dto.input.UpdateUserInfoInput;
import com.example.kotkit.dto.response.UserProfileResponse;
import com.example.kotkit.dto.response.UserResponse;
import com.example.kotkit.entity.Friendship;
import com.example.kotkit.entity.Users;
import com.example.kotkit.entity.enums.FriendshipStatus;
import com.example.kotkit.exception.AppException;
import com.example.kotkit.repository.FriendshipRepository;
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

    public int getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public UserResponse updateCurrentUser(UpdateUserInfoInput input) {
        int meId = getCurrentUserId();
        Users user = getUser(meId);

        mapper.map(input, user);
        userRepository.save(user);

        return mapper.map(user, UserResponse.class);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public Users getUser(int userId) {
        return userRepository.findById(userId).orElseThrow(() -> new AppException(404, USER_NOT_FOUND));
    }

    public List<Users> searchUsers(String query) {
        if (query == null) {
            return userRepository.findAll();
        }
        return userRepository.searchUsers(query);
    }

    public UserResponse getUserResponse(int userId) {
        Users users = getUser(userId);

        return mapper.map(users, UserResponse.class);
    }

    public UserProfileResponse getUserProfile(int userId) {
        UserResponse user = getUserResponse(userId);

        int numberOfFriends = friendshipRepository.countNumberOfFriends(userId);

        Friendship friendship = friendshipRepository.findFriendship(getCurrentUser().getId(), userId).orElse(null);
        FriendshipStatus friendshipStatus = friendship == null ? null : friendship.getStatus();

        return new UserProfileResponse(user, numberOfFriends, friendshipStatus);
    }
}
