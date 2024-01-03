package com.wukef.hhforumservice.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment_report")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reportId;

    @Column(name = "comment_id")
    private Integer commentId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "administrator_id")
    private Integer administratorId;

    @Column(name = "report_time")
    private LocalDateTime reportTime;

    @Column(name = "report_reason")
    private String reportReason;

    @Column(name = "report_status")
    private Integer reportStatus;

    @Column(name = "report_respond")
    private String reportRespond;

    @Column(name = "report_respond_time")
    private LocalDateTime reportRespondTime;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "administrator_id", insertable = false, updatable = false)
//    private Administrator administrator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", insertable = false, updatable = false)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
}