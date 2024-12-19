package com.example.kotkit.repository;

import com.example.kotkit.dto.response.UserDetailsResponse;
import com.example.kotkit.entity.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Integer> {
    @Query("""
            select f from Friendship f
            where (f.user1Id = :userId)
            and f.status = 'FRIEND'
            """)
    List<Friendship> getFriendsOfUser(@Param("userId") int userId);

    @Query("""
            select f from Friendship f
            where f.user1Id = :user1Id
            and f.user2Id = :user2Id
            """)
    Optional<Friendship> findFriendship(@Param("user1Id") int user1Id, @Param("user2Id") int user2Id);

    @Query("""
            select new com.example.kotkit.dto.response.UserDetailsResponse(
            new com.example.kotkit.dto.response.UserInfoResponse(u.id, u.email, u.fullName, u.avatar, u.birthday, u.gender, u.numberOfFriends, u.numberOfVideos, u.isBlocked, u.isVerified),
            (select f.status from Friendship f where f.user2Id = u.id and f.user1Id = :meId)
            )
            from Users u
            where u.id in (
                select f.user2Id from Friendship f
                where f.user1Id = :userId and f.status = 'FRIEND'
            )
            """)
    List<UserDetailsResponse> findFriendsOfUser(@Param("userId") int userId, @Param("meId") int meId);

}
