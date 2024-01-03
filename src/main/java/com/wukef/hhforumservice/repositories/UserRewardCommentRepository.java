package com.wukef.hhforumservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.wukef.hhforumservice.entities.UserRewardComment;

import java.util.List;

@Repository
public interface UserRewardCommentRepository extends JpaRepository<UserRewardComment, Integer> {
    List<UserRewardComment> findAllByCommentIdInAndRewardType(List<Integer> commentIds, String rewardType);
    long countByCommentIdInAndRewardType(List<Integer> commentIds, String rewardType);
}