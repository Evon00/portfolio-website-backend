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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
S3Uploader
- S3 Client를 통해 이미지 객체 업로드, 삭제 구현
 */
@Component
@RequiredArgsConstructor
public class S3Uploader {

    private static final Set<String> ALLOWED_EXT = Set.of("jpg", "jpeg", "png", "webp", "svg");
    private final S3Client s3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    @Value("${cloud.aws.cloudfront}")
    private String cloudfront;

    private static String getExtension(String name) {
        int i = name.lastIndexOf('.');
        return (i >= 0) ? name.substring(i + 1) : "";
    }

    private static String generateS3Key(String keyPrefix, String slug, String ext) {
        String key;

        if (slug.isEmpty())
            key = keyPrefix + "/" + UUID.randomUUID() + "." + ext;
        else
            key = keyPrefix + "/" + slug + "/" + UUID.randomUUID() + "." + ext;

        return key;
    }

    /*
    S3 image upload
    - [param] MultipartFile , keyPrefix("profile","project","post", "icon"), slug(각 키별 고유값)
    - [return] ImageMetaDataDTO
    - S3에 svg 형태 파일 추가
     */
    public ImageMetaDataDTO upload(MultipartFile file, String keyPrefix, String slug) throws IOException {


        String originalName = Optional.of(file.getOriginalFilename()).orElse("Unnamed");
        String ext = getExtension(originalName).toLowerCase();
        ImageMetaDataDTO dto = null;

        if (!ALLOWED_EXT.contains(ext)) {
            throw new BusinessException(ExceptionCode.INVALID_IMG_EXT);
        }

        String contentType = Optional.ofNullable(file.getContentType())
                .orElseGet(() -> switch (ext) {
                    case "jpg", "jpeg" -> "image/jpeg";
                    case "png" -> "image/png";
                    case "webp" -> "image/webp";
                    case "svg" -> "image/svg";
                    default -> "application/octet-stream";
                });

        String key = generateS3Key(keyPrefix, slug, ext);
        Path temp = Files.createTempFile("upload-", "." + ext);

        try {
            file.transferTo(temp);

            ImageDimensions dimensions = extractImageDimensions(temp, ext);


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
                    .uploadUrl(cloudfront + "/" + key)
                    .s3Key(key)
                    .imageHeight(dimensions.height)
                    .imageWidth(dimensions.width)
                    .fileSize(size)
                    .fileExtension(ext)
                    .build();

        } catch (Exception e) {
            try {
                s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(key).build());
            } catch (Exception e1) {
                throw new BusinessException(ExceptionCode.FAILED_IMG_DELETE);
            }
        } finally {
            Files.deleteIfExists(temp);
        }

        return dto;
    }

    /*
    S3 image delete
    - [param] key
    - [return] void
     */
    public void delete(String key) {

        s3Client.deleteObject(
                DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build()
        );

    }

    private ImageDimensions extractImageDimensions(Path filePath, String ext) {
        if ("svg".equals(ext)) {
            return extractSvgDimensions(filePath);
        } else {
            return extractRasterImageDimensions(filePath);
        }
    }

    private ImageDimensions extractSvgDimensions(Path filePath) {
        try {
            String svgContent = Files.readString(filePath, StandardCharsets.UTF_8);
            return parseSvgDimensions(svgContent);
        } catch (Exception e) {
            System.err.println("Failed to extract SVG dimensions: " + e.getMessage());
            return new ImageDimensions(24, 24);
        }
    }

    private ImageDimensions parseSvgDimensions(String svgContent) {
        try {
            // 정규식으로 width, height 추출
            Pattern widthPattern = Pattern.compile("width=[\"']([^\"']+)[\"']", Pattern.CASE_INSENSITIVE);
            Pattern heightPattern = Pattern.compile("height=[\"']([^\"']+)[\"']", Pattern.CASE_INSENSITIVE);
            Pattern viewBoxPattern = Pattern.compile("viewBox=[\"']([^\"']+)[\"']", Pattern.CASE_INSENSITIVE);

            Matcher widthMatcher = widthPattern.matcher(svgContent);
            Matcher heightMatcher = heightPattern.matcher(svgContent);
            Matcher viewBoxMatcher = viewBoxPattern.matcher(svgContent);

            Integer width = null;
            Integer height = null;

            if (widthMatcher.find()) {
                width = parseNumericValue(widthMatcher.group(1));
            }
            if (heightMatcher.find()) {
                height = parseNumericValue(heightMatcher.group(1));
            }

            // width, height가 없으면 viewBox에서 추출
            if ((width == null || height == null) && viewBoxMatcher.find()) {
                String viewBox = viewBoxMatcher.group(1);
                String[] values = viewBox.trim().split("\\s+");
                if (values.length >= 4) {
                    try {
                        if (width == null) {
                            width = (int) Math.ceil(Double.parseDouble(values[2]));
                        }
                        if (height == null) {
                            height = (int) Math.ceil(Double.parseDouble(values[3]));
                        }
                    } catch (NumberFormatException e) {
                        throw new BusinessException(ExceptionCode.FAILED_IMG_DECODE);
                    }
                }
            }

            // 기본값 설정
            if (width == null) width = 24;
            if (height == null) height = 24;

            return new ImageDimensions(width, height);

        } catch (Exception e) {
            return new ImageDimensions(24, 24); // 파싱 실패 시 기본값
        }
    }

    private Integer parseNumericValue(String value) {
        try {
            // px, em, %, pt 등 단위 제거
            String numericOnly = value.replaceAll("[^0-9.]", "");
            if (numericOnly.isEmpty()) return null;

            return (int) Math.ceil(Double.parseDouble(numericOnly));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private ImageDimensions extractRasterImageDimensions(Path filePath) {
        try {
            BufferedImage image = ImageIO.read(filePath.toFile());
            if (image == null) {
                throw new BusinessException(ExceptionCode.INVALID_IMG_EXT);
            }
            return new ImageDimensions(image.getWidth(), image.getHeight());
        } catch (Exception e) {
            throw new BusinessException(ExceptionCode.FAILED_IMG_DECODE);
        }
    }

    private record ImageDimensions(int width, int height) {
    }
}
