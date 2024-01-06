package com.wukef.hhforumservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.wukef.hhforumservice.entities.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    int countByPostIdAndAuthorIdNot(Integer postId, Integer authorId);
    @Query("SELECT c FROM Comment c WHERE c.post.postId = :postId ORDER BY c.commentTime DESC")
    List<Comment> findByPostIdOrderByCommentTimeDesc(@Param("postId") Integer postId);
    List<Comment> findByPostIdOrderByFloorNumberAsc(Integer postId);
}