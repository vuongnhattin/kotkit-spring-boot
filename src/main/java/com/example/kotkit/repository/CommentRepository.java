package com.example.kotkit.repository;

import com.example.kotkit.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Modifying
    @Query("DELETE FROM Comment c WHERE c.commentId = :commentId AND c.authorId = :authorId")
    @Transactional
    void deleteByCommentIdAndAuthorId(@Param("commentId") Integer commentId,
                                      @Param("authorId") Integer authorId);
}
