package com.wukef.hhforumservice.entities;

import lombok.*;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;

    @Column(name = "author_id", nullable = false)
    private Integer authorId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "is_bounty")
    private Boolean isBounty;

    @Column(name = "censor_status")
    private Integer censorStatus;

    @Column(name = "total_floor", nullable = false)
    private Integer totalFloor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "userId", insertable = false, updatable = false)
    private User author;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private PostWithBounty postWithBounty;

    @ManyToMany
    @JoinTable(
            name = "post_post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "post_tag_id")
    )
    private Set<PostTag> postTags = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_posts",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users = new HashSet<>();
}