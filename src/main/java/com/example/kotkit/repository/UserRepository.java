package com.example.kotkit.repository;

import com.example.kotkit.dto.response.UserDetailsResponse;
import com.example.kotkit.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("""
            select new com.example.kotkit.dto.response.UserDetailsResponse(
                new com.example.kotkit.dto.response.UserInfoResponse(u.id, u.email, u.fullName, u.avatar, u.birthday, u.gender, u.numberOfFriends, u.numberOfVideos, u.isBlocked, u.isVerified),
                (select f.status from Friendship f where f.user2Id = u.id and f.user1Id = :meId)
            )
            from Users u
            where lower(u.email) like lower(concat('%', :query, '%'))
            or lower(u.fullName) like lower(concat('%', :query, '%'))
            """)
    List<UserDetailsResponse> searchUsers(@Param("query") String query, @Param("meId") int meId);

    @Query("""
            select new com.example.kotkit.dto.response.UserDetailsResponse(
                new com.example.kotkit.dto.response.UserInfoResponse(u.id, u.email, u.fullName, u.avatar, u.birthday, u.gender, u.numberOfFriends, u.numberOfVideos, u.isBlocked, u.isVerified),            
                (select f.status from Friendship f where f.user2Id = u.id and f.user1Id = :meId)
            )
            from Users u
            """)
    List<UserDetailsResponse> getAllUsers(@Param("meId") int meId);

    @Query("""
            select new com.example.kotkit.dto.response.UserDetailsResponse(
                new com.example.kotkit.dto.response.UserInfoResponse(u.id, u.email, u.fullName, u.avatar, u.birthday, u.gender, u.numberOfFriends, u.numberOfVideos, u.isBlocked, u.isVerified),            
                (select f.status from Friendship f where f.user2Id = u.id and f.user1Id = :meId)
            )
            from Users u
            where u.id = :userId
            """)
    UserDetailsResponse getUserDetailsById(@Param("userId") int userId, @Param("meId") int meId);
}
