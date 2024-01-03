package com.wukef.hhforumservice.dto;

import com.wukef.hhforumservice.entities.Comment;
import lombok.Data;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class FloorData {
    private String postTime;
    private Integer floorNumber;
    private String content;
    private Integer commentId;
    private List<ReplyData> comments;
    private AuthorData author;
    private RewardData reward;

    public void autoSet(Comment comment) {
        this.postTime = comment.getCommentTime().toString();
        this.floorNumber = comment.getFloorNumber();
        this.content = comment.getContent();
        this.commentId = comment.getCommentId();
        this.comments = comment.getCommentReports().stream()
                .map(reply -> new ReplyData().autoSet(reply.getComment()))
                .collect(Collectors.toList());
    }
}