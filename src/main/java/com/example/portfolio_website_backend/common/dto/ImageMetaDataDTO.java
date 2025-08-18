package com.example.portfolio_website_backend.common.dto;

import lombok.Builder;

public record ImageMetaDataDTO(
        String originalFilename,
        String uploadUrl,
        String s3Key,
        int imageWidth,
        int imageHeight,
        Long fileSize,
        String fileExtension

) {

    @Builder
    public ImageMetaDataDTO(String originalFilename, String uploadUrl, String s3Key, int imageWidth, int imageHeight, Long fileSize, String fileExtension){
        this.originalFilename = originalFilename;
        this.uploadUrl = uploadUrl;
        this.s3Key = s3Key;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.fileSize = fileSize;
        this.fileExtension = fileExtension;
    }
}
