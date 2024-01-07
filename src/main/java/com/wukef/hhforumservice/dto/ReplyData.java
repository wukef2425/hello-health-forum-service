package com.wukef.hhforumservice.dto;

import com.wukef.hhforumservice.entities.Comment;
import lombok.Data;

@Data
public class ReplyData {
    private String content;
    private Integer commentId;
    private String postTime;
    private Integer commentUserId;
    private String commentUserName;

    public ReplyData autoSet(Comment comment) {
        this.content = comment.getContent();
        this.commentId = comment.getCommentId();
        this.postTime = comment.getCommentTime().toString();
        this.commentUserId = comment.getAuthorId();
        this.commentUserName = comment.getAuthor().getUserName();
        return this;
    }
}