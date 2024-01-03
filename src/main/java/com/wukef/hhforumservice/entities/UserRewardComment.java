package com.wukef.hhforumservice.entities;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_reward_comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRewardComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rewardId;

    @Column(name = "comment_id")
    private Integer commentId;

    @Column(name = "reward_time")
    private LocalDateTime rewardTime;

    @Column(name = "reward_type", nullable = false)
    private String rewardType;

    @Column(name = "reward_value", nullable = false)
    private Integer rewardValue;

    @Column(name = "giver_user_id")
    private Integer giverUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", referencedColumnName = "commentId", insertable = false, updatable = false)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "giver_user_id", referencedColumnName = "userId", insertable = false, updatable = false)
    private User giverUser;
}