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
            u,
            (select f.status from Friendship f where f.user2Id = u.userId and f.user1Id = :meId)
            )
            from Users u
            where u.userId in (
                select f.user2Id from Friendship f
                where f.user1Id = :userId and f.status = 'FRIEND'
            )
            """)
    List<UserDetailsResponse> findFriendsOfUser(@Param("userId") int userId, @Param("meId") int meId);

}
