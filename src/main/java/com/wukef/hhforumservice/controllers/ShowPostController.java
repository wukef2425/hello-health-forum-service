package com.wukef.hhforumservice.controllers;

import com.wukef.hhforumservice.dto.PostData;
import com.wukef.hhforumservice.entities.Message;
import com.wukef.hhforumservice.entities.Post;
import com.wukef.hhforumservice.entities.User;
import com.wukef.hhforumservice.repositories.PostRepository;
import com.wukef.hhforumservice.repositories.UserRepository;
import com.wukef.hhforumservice.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/forum/posts")
public class ShowPostController {
    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> sortAndShowPosts(
            @RequestParam(required = false) String sort) {
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
}