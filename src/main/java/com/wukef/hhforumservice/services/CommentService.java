package com.wukef.hhforumservice.services;

import com.wukef.hhforumservice.dto.AuthorData;
import com.wukef.hhforumservice.dto.FloorData;
import com.wukef.hhforumservice.dto.ReplyData;
import com.wukef.hhforumservice.dto.RewardData;
import com.wukef.hhforumservice.entities.Comment;
import com.wukef.hhforumservice.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public List<FloorData> processComments(Integer postId) {
        List<Comment> comments = commentRepository.findByPostIdOrderByFloorNumberAsc(postId);

        // 创建一个Map来存储楼层ID和对应的评论列表
        Map<Integer, List<Comment>> commentsGroupedByFloor = comments.stream()
                .filter(comment -> comment.getParentCommentId() != null)
                .collect(Collectors.groupingBy(Comment::getParentCommentId));

        List<FloorData> floorDataList = comments.stream()
                .filter(comment -> comment.getParentCommentId() == null && comment.getCensorStatus() == 1)
                .map(floorComment -> {
                    FloorData floorData = new FloorData();
                    floorData.autoSet(floorComment);

                    AuthorData authorData = new AuthorData();
                    authorData.autoSet(floorComment.getAuthor(), 205);
                    floorData.setAuthor(authorData);

                    List<Comment> floorComments = commentsGroupedByFloor.getOrDefault(floorComment.getCommentId(), Collections.emptyList());
                    List<ReplyData> commentDataList = floorComments.stream()
                            .map(subComment -> {
                                ReplyData commentData = new ReplyData();
                                commentData.autoSet(subComment);
                                return commentData;
                            })
                            .collect(Collectors.toList());

                    floorData.setComments(commentDataList); // 设置楼层的评论列表

                    return floorData;
                })
                .collect(Collectors.toList());

        return floorDataList;
    }
}