package com.example.portfolio_website_backend.post.domain;

import com.example.portfolio_website_backend.member.domain.Member;
import com.example.portfolio_website_backend.skill.domain.Skill;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String summary;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "read_time", nullable = false)
    private String readTime;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "modified_at", nullable = false)
    private Instant modifiedAt;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(nullable = false)
    private Long view;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<PostSkill> postSkills = new ArrayList<>();

    @Builder
    public Post(Member member, String title, String summary, String content, String readTime, String slug) {
        this.member = member;
        this.title = title;
        this.summary = summary;
        this.content = content;
        this.readTime = readTime;
        this.slug = slug;
        this.view = 0L;
    }

    public void addPostSkill(Skill skill) {
        PostSkill postSkill = PostSkill.builder()
                .post(this)
                .skill(skill)
                .build();
        this.postSkills.add(postSkill);
    }

    public void removePostSkill(PostSkill postSkill) {
        this.postSkills.remove(postSkill);
    }

}
