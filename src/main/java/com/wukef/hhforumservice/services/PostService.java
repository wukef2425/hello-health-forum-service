package com.wukef.hhforumservice.services;

import com.wukef.hhforumservice.entities.Comment;
import com.wukef.hhforumservice.entities.Post;
import com.wukef.hhforumservice.entities.UserRewardComment;
import com.wukef.hhforumservice.repositories.CommentRepository;
import com.wukef.hhforumservice.repositories.UserRewardCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class PostService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRewardCommentRepository userRewardCommentRepository;

    public double calcHeat(Post post) {
        List<Integer> ids = post.getComments().stream()
                .filter(c -> c.getCensorStatus() == 1)
                .map(c -> c.getCommentId())
                .collect(Collectors.toList());

        int coins = userRewardCommentRepository.findAllByCommentIdInAndRewardType(ids, "coin")
                .stream()
                .mapToInt(UserRewardComment::getRewardValue)
                .sum();

        int likes = (int) userRewardCommentRepository.countByCommentIdInAndRewardType(ids, "like");

        int replies = commentRepository.countByPostIdAndAuthorIdNot(post.getPostId(), post.getAuthor().getUserId());

        final double weightCoin = 1.5;
        final double weightLike = 1.0;
        final double weightReply = 1.0;

        return coins * weightCoin + likes * weightLike + replies * weightReply;
    }

    public Date getNewestComment(Post post) {
        List<Comment> comments = commentRepository.findByPostIdOrderByCommentTimeDesc(post.getPostId());

        if (!comments.isEmpty()) {
            // 获取列表中的第一个元素，即时间最新的评论
            Comment newestComment = comments.get(0);
            // 转换评论时间到Date对象
            return Date.from(newestComment.getCommentTime()
                    .atZone(ZoneId.systemDefault())
                    .toInstant());
        }

        return null; // 如果没有评论，返回null
    }
}