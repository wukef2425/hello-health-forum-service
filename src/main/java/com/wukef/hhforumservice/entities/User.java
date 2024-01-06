package com.wukef.hhforumservice.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;

@Entity
@Table(name = "user_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "gender")
    private String gender;

    @Column(name = "portait", nullable = false)
    private String portrait;

    @Column(name = "birthday")
    private LocalDateTime birthday;

    @Column(name = "signature")
    private String signature;

    @Column(name = "hb_number", nullable = false)
    private BigDecimal hbNumber;

    @Column(name = "fan_number", nullable = false)
    private BigDecimal fanNumber;

    @Column(name = "follow_number", nullable = false)
    private BigDecimal followNumber;

    @Column(name = "is_approved", nullable = false)
    private String isApproved;

    @Column(name = "last_login_time", nullable = false)
    private LocalDateTime lastLoginTime;

    @Column(name = "is_locked")
    private LocalDateTime isLocked;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentReport> commentReports;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    @OneToMany(mappedBy = "giverUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRewardComment> userRewardComments;

    @ManyToMany
    @JoinTable(
            name = "follow_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "followed_id")
    )
    private List<User> followeds; // The users that this user is following

    @ManyToMany(mappedBy = "followeds")
    private List<User> fans; // The fans of this user (users that follow this user)
}