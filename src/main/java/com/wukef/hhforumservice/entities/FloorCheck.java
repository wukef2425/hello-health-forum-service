package com.wukef.hhforumservice.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "floor_checks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FloorCheck {

    @Id
    @Column(name = "comment_id")
    private int commentId;

    @Column(name = "administrator_id")
    private Integer administratorId;

    @Column(name = "review_time")
    private LocalDateTime reviewTime;

    @Column(name = "review_status")
    private Integer reviewStatus;

    @Column(name = "review_reason")
    private String reviewReason;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "administrator_id", insertable = false, updatable = false)
//    private Administrator administrator;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "comment_id")
    private Comment comment;
}