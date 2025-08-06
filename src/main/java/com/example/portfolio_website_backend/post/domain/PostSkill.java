package com.example.portfolio_website_backend.post.domain;

import com.example.portfolio_website_backend.skill.domain.Skill;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "post_skill", uniqueConstraints = @UniqueConstraint(columnNames = {"post_id","skill_id"}))
public class PostSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;

    @Builder
    public PostSkill(Post post, Skill skill){
        this.post = post;
        this.skill = skill;
    }

    protected void setPost(Post post){
        this.post = post;
    }
    protected void setSkill(Skill skill){
        this.skill = skill;
    }
}
