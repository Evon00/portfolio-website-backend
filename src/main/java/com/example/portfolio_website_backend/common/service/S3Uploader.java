package com.example.portfolio_website_backend.common.service;

import com.example.portfolio_website_backend.common.dto.ImageMetaDataDTO;
import com.example.portfolio_website_backend.common.exception.BusinessException;
import com.example.portfolio_website_backend.common.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/*
S3Uploader
- S3 Client를 통해 이미지 객체 업로드, 삭제 구현
 */
@Component
@RequiredArgsConstructor
public class S3Uploader {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    @Value("${cloud.aws.cloudfront}")
    private String cloudfront;

    private final S3Client s3Client;
    private static final Set<String> ALLOWED_EXT = Set.of("jpg","jpeg","png","webp");


    /*
    S3 image upload
    - [param] MultipartFile , keyPrefix("profile","project","post"), slug(각 키별 고유값)
    - [return] ImageMetaDataDTO
     */
    public ImageMetaDataDTO upload(MultipartFile file, String keyPrefix, String slug) throws IOException {


        String originalName = Optional.of(file.getOriginalFilename()).orElse("Unnamed");
        String ext = getExtension(originalName).toLowerCase();
        ImageMetaDataDTO dto = null;

        if(!ALLOWED_EXT.contains(ext)){
            throw new BusinessException(ExceptionCode.INVALID_IMG_EXT);
        }

        String contentType = Optional.ofNullable(file.getContentType())
                .orElseGet(() -> switch (ext) {
                    case "jpg", "jpeg" -> "image/jpeg";
                    case "png" -> "image/png";
                    case "webp" -> "image/webp";
                    default -> "application/octet-stream";
                });

        String key = generateS3Key(keyPrefix,slug,ext);
        Path temp = Files.createTempFile("upload-", "." + ext);

        try {
            file.transferTo(temp);
            int width, height = 0;

            try {
                BufferedImage image = ImageIO.read(temp.toFile());
                if(image == null) throw new BusinessException(ExceptionCode.INVALID_IMG_EXT);
                width = image.getWidth();
                height = image.getHeight();
            } catch (Exception e){
                throw new BusinessException(ExceptionCode.FAILED_IMG_DECODE);

            }

            PutObjectRequest put = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(contentType)
                    .cacheControl("public, max-age=31536000, immutable")
                    .build();

            s3Client.putObject(put, RequestBody.fromFile(temp));

            Long size = Files.size(temp);

            dto = ImageMetaDataDTO.builder()
                    .originalFilename(originalName)
                    .uploadUrl(cloudfront+"/"+key)
                    .s3Key(key)
                    .imageHeight(height)
                    .imageWidth(width)
                    .fileSize(size)
                    .fileExtension(ext)
                    .build();

        } catch (Exception e){
            try{
                s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(key).build());
            } catch (Exception e1){
                throw new BusinessException(ExceptionCode.FAILED_IMG_DELETE);
            }
        } finally{
            Files.deleteIfExists(temp);
        }

        return dto;
    }

    /*
    S3 image delete
    - [param] key
    - [return] void
     */
    public void delete(String key){

        s3Client.deleteObject(
                DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build()
        );

    }

    private static String getExtension(String name) {
        int i = name.lastIndexOf('.');
        return (i >= 0) ? name.substring(i + 1) : "";
    }

    private static String generateS3Key(String keyPrefix, String slug, String ext){
        String key;

        if(slug.isEmpty())
            key = keyPrefix + "/" + UUID.randomUUID() + "." + ext;
        else
            key = keyPrefix + "/" + slug + "/" +UUID.randomUUID() + "." + ext;

        return key;
    }
}
