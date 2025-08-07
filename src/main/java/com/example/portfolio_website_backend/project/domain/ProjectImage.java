package com.example.portfolio_website_backend.project.domain;

import com.example.portfolio_website_backend.common.domain.ImageContent;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@SuperBuilder
public class ProjectImage extends ImageContent {

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "display_order")
    private int displayOrder;

}
