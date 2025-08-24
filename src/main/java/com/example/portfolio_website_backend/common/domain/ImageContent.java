package com.example.portfolio_website_backend.common.domain;


import com.example.portfolio_website_backend.common.dto.ImageMetaDataDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder //자식이 빌더패턴 이용시, 부모 필드 이용하도록
public abstract class ImageContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "original_filename", nullable = false)
    private String originalFilename;

    @Column(name = "upload_url", nullable = false)
    private String uploadUrl;

    @Column(name = "s3_key", nullable = false)
    private String s3Key;

    @Column(name = "image_width", nullable = false)
    private int imageWidth;

    @Column(name = "image_height", nullable = false)
    private int imageHeight;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "file_extension", nullable = false)
    private String fileExtension;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private Instant createdAt;


    public void updateFile(ImageMetaDataDTO dto) {
        this.originalFilename = dto.originalFilename();
        this.uploadUrl = dto.uploadUrl();
        this.s3Key = dto.s3Key();
        this.imageWidth = dto.imageWidth();
        this.imageHeight = dto.imageHeight();
        this.fileSize = dto.fileSize();
        this.fileExtension = dto.fileExtension();
    }

}
