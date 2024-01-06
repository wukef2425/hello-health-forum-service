package com.wukef.hhforumservice.controllers;

import com.wukef.hhforumservice.entities.Comment;
import com.wukef.hhforumservice.entities.CommentReport;
import com.wukef.hhforumservice.entities.Message;
import com.wukef.hhforumservice.repositories.CommentReportRepository;
import com.wukef.hhforumservice.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/forum/comments")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentReportRepository commentReportRepository;

    @PostMapping("/{commentId}")
    public ResponseEntity<String> reportComment(
            @PathVariable int commentId,
            @RequestParam(name = "reporting", required = false) Boolean reporting,
            @RequestBody Map<String, Object> frontEndData) {
        // http://localhost:8089/forum/comments/76?reporting=true

        if (reporting == null || !reporting) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Invalid request: missing or false 'report' query parameter.");
        }

        Message responseMessage = new Message();

        try {
            String reason = (String) frontEndData.get("reason");
            Integer userId = 205;

            if (userId == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(responseMessage.returnJson(401));
            }

            Optional<Comment> commentOptional = commentRepository.findById(commentId);
            if (!commentOptional.isPresent()) {
                responseMessage.setMessage("评论不存在。");
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(responseMessage.returnJson(404));
            }

            Comment comment = commentOptional.get();
            if (commentReportRepository.existsByCommentIdAndReportStatusAndUserId(commentId, 2, userId)) {
                responseMessage.setMessage("我们已经收到了您的举报！请静候审核结果，不要重复举报。");
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(responseMessage.returnJson(409));
            }

            if (comment.getAuthorId() == userId) {
                responseMessage.setMessage("如果您对自己的内容不满意可以直接删除，不用进行举报。");
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(responseMessage.returnJson(400));
            }

            CommentReport report = new CommentReport();
            report.setCommentId(commentId);
            report.setUserId(userId);
            report.setReportReason(reason);

            int pendingStatus = 2; // 状态 '2' 表示 '待处理'
            report.setReportStatus(pendingStatus); // 设置举报状态

            commentReportRepository.save(report);

            responseMessage.setMessage("举报成功！");
            return ResponseEntity
                    .ok(responseMessage.returnJson(200));
        } catch (Exception e) {
            responseMessage.setMessage("An error occurred while processing your report.");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(responseMessage.returnJson(500));
        }
    }
}