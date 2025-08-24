package com.example.portfolio_website_backend.post.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyPostViews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "view_date", nullable = false)
    private Instant viewDate;

    @Column(name = "view_count", nullable = false)
    private Long viewCount;

    @Column(name = "uniqueVisitors", nullable = false)
    private Long uniqueVisitors;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Builder
    public DailyPostViews(Post post, Instant viewDate, Long viewCount, Long uniqueVisitors, Instant createdAt) {
        this.post = post;
        this.viewDate = viewDate;
        this.viewCount = viewCount;
        this.uniqueVisitors = uniqueVisitors;
        this.createdAt = createdAt;
    }
}
