package com.wukef.hhforumservice.repositories;

import com.wukef.hhforumservice.entities.CommentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.wukef.hhforumservice.entities.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentReportRepository extends JpaRepository<CommentReport, Integer> {
    boolean existsByCommentIdAndReportStatusAndUserId(Integer commentId, Integer reportStatus, Integer userId);
}