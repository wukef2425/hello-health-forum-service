package com.wukef.hhforumservice.entities;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "post_with_bounties")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostWithBounty {

    @Id
    @Column(name = "post_id")
    private Integer postId;

    @Column(name = "bounty_value", nullable = false)
    private Integer bountyValue;

    @Column(name = "best_answer_comment_id")
    private Integer bestAnswerCommentId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "best_answer_comment_id", referencedColumnName = "commentId")
    private Comment bestAnswerComment;
}