package com.example.kotkit.service;

import com.example.kotkit.dto.response.UserDetailsResponse;
import com.example.kotkit.entity.Friendship;
import com.example.kotkit.entity.enums.FriendshipStatus;
import com.example.kotkit.exception.AppException;
import com.example.kotkit.repository.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FriendshipService {
    public static final String FRIENDSHIP_FAILED = "FAILED";
    private final FriendshipRepository friendshipRepository;
    private final UserService userService;
    private final ModelMapper mapper;

    public List<UserDetailsResponse> getFriendsOfUser(int userId) {
        return friendshipRepository.findFriendsOfUser(userId, userService.getMeId());
    }

    public boolean isFriend(int user1Id, int user2Id) {
        Optional<Friendship> friendship = friendshipRepository.findFriendship(user1Id, user2Id);
        return friendship.isPresent() && friendship.get().getStatus() == FriendshipStatus.FRIEND;
    }

    public boolean existsFriendship(int user1Id, int user2Id) {
        Optional<Friendship> friendship = friendshipRepository.findFriendship(user1Id, user2Id);
        return friendship.isPresent();
    }

    public void sendFriendRequest(int userId) {
        int meId = userService.getMeId();

        if (meId == userId || existsFriendship(meId, userId)) {
            throw new AppException(400, FRIENDSHIP_FAILED);
        }

        Friendship friendship1 = Friendship.builder()
                .user1Id(meId)
                .user2Id(userId)
                .status(FriendshipStatus.SENT)
                .build();

        Friendship friendship2 = Friendship.builder()
                .user1Id(userId)
                .user2Id(meId)
                .status(FriendshipStatus.RECEIVED)
                .build();


        friendshipRepository.save(friendship1);
        friendshipRepository.save(friendship2);
    }

    public void acceptFriendRequest(int userId) {
        int meId = userService.getMeId();

        if (!existsFriendship(userId, meId)) {
            throw new AppException(400, FRIENDSHIP_FAILED);
        }

        Friendship friendship1 = friendshipRepository.findFriendship(meId, userId).get();
        Friendship friendship2 = friendshipRepository.findFriendship(userId, meId).get();

        if (friendship1.getStatus() != FriendshipStatus.RECEIVED || friendship2.getStatus() != FriendshipStatus.SENT) {
            throw new AppException(400, FRIENDSHIP_FAILED);
        }

        friendship1.setStatus(FriendshipStatus.FRIEND);
        friendship2.setStatus(FriendshipStatus.FRIEND);

        friendshipRepository.save(friendship1);
        friendshipRepository.save(friendship2);

        userService.increaseNumberOfFriends(meId);
        userService.increaseNumberOfFriends(userId);
    }

    public void rejectFriendRequest(int userId) {
        int meId = userService.getMeId();

        if (meId == userId || !existsFriendship(userId, meId)) {
            throw new AppException(400, FRIENDSHIP_FAILED);
        }

        Friendship friendship1 = friendshipRepository.findFriendship(meId, userId).get();
        Friendship friendship2 = friendshipRepository.findFriendship(userId, meId).get();

        if (friendship1.getStatus() != FriendshipStatus.RECEIVED || friendship2.getStatus() != FriendshipStatus.SENT) {
            throw new AppException(400, FRIENDSHIP_FAILED);
        }

        friendshipRepository.delete(friendship1);
        friendshipRepository.delete(friendship2);
    }

    public void takeBackFriendRequest(int userId) {
        int meId = userService.getMeId();

        if (meId == userId || !existsFriendship(meId, userId)) {
            throw new AppException(400, FRIENDSHIP_FAILED);
        }

        Friendship friendship1 = friendshipRepository.findFriendship(meId, userId).get();
        Friendship friendship2 = friendshipRepository.findFriendship(userId, meId).get();

        if (friendship1.getStatus() != FriendshipStatus.SENT || friendship2.getStatus() != FriendshipStatus.RECEIVED) {
            throw new AppException(400, FRIENDSHIP_FAILED);
        }

        friendshipRepository.delete(friendship1);
        friendshipRepository.delete(friendship2);
    }

    public void unfriend(int userId) {
        int meId = userService.getMeId();

        if (meId == userId || !existsFriendship(meId, userId)) {
            throw new AppException(400, FRIENDSHIP_FAILED);
        }

        Friendship friendship1 = friendshipRepository.findFriendship(meId, userId).get();
        Friendship friendship2 = friendshipRepository.findFriendship(userId, meId).get();

        if (friendship1.getStatus() != FriendshipStatus.FRIEND || friendship2.getStatus() != FriendshipStatus.FRIEND) {
            throw new AppException(400, FRIENDSHIP_FAILED);
        }

        friendshipRepository.delete(friendship1);
        friendshipRepository.delete(friendship2);

        userService.decreaseNumberOfFriends(meId);
        userService.decreaseNumberOfFriends(userId);
    }
}