package com.example.kotkit.repository;

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
            where (f.user1Id = :user1Id and f.user2Id = :user2Id)
            """)
    Optional<Friendship> findFriendship(int user1Id, int user2Id);

    @Query("""
            select count(f) from Friendship f
            where (f.user1Id = :userId)
            and f.status = 'FRIEND'
            """)
    int countNumberOfFriends(@Param("userId") int userId);
}
