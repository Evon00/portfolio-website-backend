package com.example.portfolio_website_backend.project.domain;

import com.example.portfolio_website_backend.common.domain.ImageContent;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ProjectImage extends ImageContent {

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    private int order;
}
