package com.wukef.hhforumservice.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int commentId;

    @Column(name = "author_id")
    private int authorId;

    @Column(name = "comment_time")
    private LocalDateTime commentTime;

    @Column(name = "parent_comment_id")
    private Integer parentCommentId;

    @Column(name = "post_id")
    private int postId;

    @Column(name = "content")
    private String content;

    @Column(name = "censor_status")
    private Integer censorStatus;

    @Column(name = "floor_number")
    private Integer floorNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", insertable = false, updatable = false)
    private User author;

    @OneToMany(mappedBy = "comment")
    private List<CommentReport> commentReports;

    @OneToOne(mappedBy = "comment")
    private FloorCheck floorCheck;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", insertable = false, updatable = false)
    private Post post;

    @OneToMany(mappedBy = "comment")
    private List<PostWithBounty> postWithBounties;

    @OneToMany(mappedBy = "comment")
    private List<UserRewardComment> userRewardComments;
}