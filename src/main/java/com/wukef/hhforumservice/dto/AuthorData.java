package com.wukef.hhforumservice.dto;

import com.wukef.hhforumservice.entities.User;
import lombok.Data;

import java.util.List;

@Data
public class AuthorData {

    private Integer userId;
    private String userName;
    private String userGroup; // "normal" or "doctor"
    private String avatarUrl;
    private Boolean verified;
    private Integer follows; // Author's follow count
    private Integer follower;
    private Boolean followed; // Whether "I" am following

    public void autoSet(User author, Integer mineId) {
        this.userId = author.getUserId();
        this.userName = author.getUserName();
        this.userGroup = "n".equals(author.getIsApproved()) ? "normal" : "doctor";
        this.verified = "y".equals(author.getIsApproved());
        this.follows = author.getFollowNumber() != null ? author.getFollowNumber().intValue() : null;
        this.follower = author.getFanNumber() != null ? author.getFanNumber().intValue() : null;

        this.avatarUrl = author.getPortrait();
        if (mineId == null || mineId == -1) {
            this.followed = false;
        } else {
            List<User> fans = author.getFans();
            this.followed = fans.stream().anyMatch(b -> mineId.equals(b.getUserId()));
        }

        System.out.println("Author is " + this.userName + " and my follow status is " + this.followed);
    }
}