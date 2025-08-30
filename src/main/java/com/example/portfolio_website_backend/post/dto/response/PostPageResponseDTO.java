package com.example.portfolio_website_backend.post.dto.response;

import com.example.portfolio_website_backend.post.domain.Post;
import org.springframework.data.domain.Page;

import java.util.List;

public record PostPageResponseDTO(
        List<PostResponseDTO> posts,
        int totalPages,
        Long totalElements,
        int size,
        int number,
        boolean first,
        boolean last
) {
    public static PostPageResponseDTO create(List<PostResponseDTO> posts, Page<Post> page) {
        return new PostPageResponseDTO(
                posts,
                page.getTotalPages(),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber(),
                page.isFirst(),
                page.isLast()
        );
    }
}
