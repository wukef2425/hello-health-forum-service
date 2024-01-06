package com.wukef.hhforumservice.controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wukef.hhforumservice.dto.PostData;
import com.wukef.hhforumservice.entities.*;
import com.wukef.hhforumservice.repositories.CommentRepository;
import com.wukef.hhforumservice.repositories.FloorCheckRepository;
import com.wukef.hhforumservice.repositories.PostRepository;
import com.wukef.hhforumservice.repositories.UserRepository;
import com.wukef.hhforumservice.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/forum/posts")
public class PostController {
    @GetMapping
    public ResponseEntity<?> sortAndShowPosts(@RequestParam(required = false) String sort) {
        Message message = new Message();
        message.setErrorCode(500);

        try {
            Integer userId = 205;

            if (userId == null) {
                System.out.println("user ID为空，但需要显示所有帖子");
            }

            User user = new User();
            user.setUserId(-1); // 表示该用户未登录

            if (userId != null) {
                user = userRepository.findById(userId).orElse(null);
            }

            List<PostData> postDataList = new ArrayList<>();
            List<Post> posts = postRepository.findAllByCensorStatus(1);

            if ("time".equalsIgnoreCase(sort)) {
                posts.sort(Comparator.comparing(postService::getNewestComment,
                        Comparator.nullsLast(Comparator.naturalOrder())).reversed());
            } else if ("popularity".equalsIgnoreCase(sort)) {
                posts.sort(Comparator.comparing(postService::calcHeat).reversed());
            }

            for (Post post : posts) {
                PostData postData = new PostData();
                postData.autoSet(post, user);
                postDataList.add(postData);
            }

            message.getData().put("post_list", postDataList);

            List<String> preTags = Arrays.asList("问诊", "求建议", "求鼓励", "经验分享", "健康科普");
            message.getData().put("tags", preTags);

            message.setErrorCode(200);
        } catch (NoSuchElementException e) {
            message.setErrorCode(400);
        } catch (IllegalStateException e) {
            System.out.println(e.toString());
            message.setErrorCode(404);
        } catch (Exception e) {
            message.setErrorCode(500);
            message.setMessage(e.getMessage());
        }

        return ResponseEntity.status(message.getErrorCode()).body(message);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostDetails(@PathVariable int postId) {
        Map<String, Object> response = new HashMap<>();
        HttpStatus status = HttpStatus.OK;

        try {
            Integer userId = 205;
            User user = userId != null ? userRepository.findById(userId).orElse(null) : null;
            if (user == null) {
                response.put("message", "User ID is null, but access to post details is required.");
                user = new User();
                user.setUserId(-1); // Indicate that the user is not logged in
            }

            Post post = postRepository.findById(postId).orElseThrow(() ->
                    new NoSuchElementException("Post with ID " + postId + " not found.")
            );

            PostData postData = new PostData();
            postData.autoSet(post, user); // Populate postData with post and user details

            response.put("post", postData);
            response.put("status", true);
        } catch (NoSuchElementException e) {
            status = HttpStatus.NOT_FOUND;
            response.put("status", false);
            response.put("message", e.getMessage());
        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            response.put("status", false);
            response.put("message", "An error occurred while fetching the post details.");
        }

        return new ResponseEntity<>(response, status);
    }

    @PostMapping
    public ResponseEntity<String> createFloor(@RequestBody String frontEndData) {
        JSONObject message = new JSONObject();
        message.put("status", false); // 默认状态为false

        try {
            JSONObject frontEndDataJson = JSON.parseObject(frontEndData);
            String content = frontEndDataJson.getString("content");
            int postId = frontEndDataJson.getIntValue("post_id");

            Integer userId = 205;

            if (userId == null) {
                System.out.println("user ID为空！");
                message.put("errorCode", 403);
                return ResponseEntity.status(403).body(message.toJSONString());
            }

            JSON.parseObject(content); // Validates JSON format

            User user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("User not found"));
            if (user.getIsLocked() != null) {
                System.out.println("用户被封禁");
                message.put("errorCode", 444);
                return ResponseEntity.status(444).body(message.toJSONString());
            }

            Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalStateException("Post not found"));
            Comment comment = new Comment();
            comment.setAuthorId(user.getUserId());
            comment.setParentCommentId(null);
            comment.setFloorNumber(post.getTotalFloor() + 1);
            post.setTotalFloor(post.getTotalFloor() + 1); // Increment total floor count for the post

            comment.setCensorStatus(2); // 设为已通过审核
            comment.setPostId(postId);
            comment.setContent(content);

            commentRepository.save(comment);

            FloorCheck fck = new FloorCheck();
            fck.setCommentId(comment.getCommentId());
            floorCheckRepository.save(fck);

            message.put("status", true);
        } catch (IllegalStateException e) {
            System.out.println(e.toString());
            message.put("errorCode", 404);
        } catch (Exception e) {
            message.put("errorCode", 500);
            message.put("message", e.getMessage());
        }
        return ResponseEntity.ok(message.toJSONString());
    }

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private FloorCheckRepository floorCheckRepository;
}