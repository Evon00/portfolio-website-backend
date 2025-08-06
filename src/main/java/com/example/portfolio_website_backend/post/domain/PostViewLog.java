package com.example.portfolio_website_backend.post.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class PostViewLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id",nullable = false)
    private Post post;

    @Column(columnDefinition = "INET", nullable = false)
    private String ipAddress;

    @Column(name = "user_agent", nullable = false)
    private String userAgent;

    @Column(nullable = false)
    private String refer;

    @Column(name = "view_at",nullable = false, updatable = false)
    @CreatedDate
    private Instant viewedAt;

    @Builder
    public PostViewLog(Post post, String ipAddress, String userAgent, String refer){
        this.post = post;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.refer = refer;
    }
}
