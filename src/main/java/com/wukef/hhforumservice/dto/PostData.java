package com.wukef.hhforumservice.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wukef.hhforumservice.entities.Post;
import com.wukef.hhforumservice.entities.User;
import com.wukef.hhforumservice.entities.Comment;
import com.wukef.hhforumservice.entities.PostTag;
import lombok.Data;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class PostData {
    private String postPic;
    private String authorName;
    private String authorPortrait;
    private List<String> postTag;
    private Integer postId;
    private String postTitle;
    private Integer postFirstCommentId;
    private RewardData reward;

    public void autoSet(Post post, User mine) {
        Comment firstComment = post.getComments().stream()
                .filter(c -> c.getFloorNumber().equals(1))
                .findFirst()
                .orElse(null);

        if (firstComment != null) {
            JSONObject contentJson = JSON.parseObject(firstComment.getContent());
            JSONArray contents = contentJson.getJSONArray("content");
            if (contents != null) {
                for (int i = 0; i < contents.size(); i++) {
                    JSONObject contentObj = contents.getJSONObject(i);
                    if ("image".equals(contentObj.getString("type"))) {
                        JSONObject attrs = contentObj.getJSONObject("attrs");
                        if (attrs != null) {
                            postPic = attrs.getString("src");
                            if (postPic != null) {
                                break;
                            }
                        }
                    }
                }
            }
        }

        if (postPic == null || postPic.isEmpty()) {
            postPic = post.getAuthor().getPortrait();
        }

        authorName = post.getAuthor().getUserName();
        authorPortrait = post.getAuthor().getPortrait();
        postTag = post.getPostTags().stream()
                .map(PostTag::getDisplayName)
                .distinct()
                .collect(Collectors.toList());
        postId = post.getPostId();
        postTitle = post.getTitle();
        postFirstCommentId = firstComment != null ? firstComment.getPostId() : null;

        reward = new RewardData();
        reward.autoSet(mine, firstComment);
    }
}