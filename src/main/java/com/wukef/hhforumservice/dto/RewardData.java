package com.wukef.hhforumservice.dto;

import com.wukef.hhforumservice.entities.Comment;
import com.wukef.hhforumservice.entities.User;
import lombok.Data;

@Data
public class RewardData {
    private LikeData like;
    private CoinData coin;

    public RewardData() {
        this.like = new LikeData();
        this.coin = new CoinData();
    }

    public void autoSet(User mine, Comment comment) {
        if (mine.getUserId() == -1) {
            this.like.setStatus(false);
        } else {
            this.like.setStatus(mine.getUserRewardComments().stream()
                    .anyMatch(b -> b.getCommentId() == comment.getCommentId() && "like".equalsIgnoreCase(b.getRewardType())));
        }
        this.like.setNum((int) comment.getUserRewardComments().stream()
                .filter(b -> "like".equalsIgnoreCase(b.getRewardType()))
                .count());

        if (mine.getUserId() == -1) {
            this.coin.setStatus(false);
        } else {
            this.coin.setStatus(mine.getUserRewardComments().stream()
                    .anyMatch(b -> b.getCommentId() == comment.getCommentId() && "coin".equalsIgnoreCase(b.getRewardType())));
        }
        this.coin.setNum(comment.getUserRewardComments().stream()
                .filter(b -> "coin".equalsIgnoreCase(b.getRewardType()))
                .mapToInt(b -> b.getRewardValue())
                .sum());
    }
}