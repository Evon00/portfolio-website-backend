package com.example.portfolio_website_backend.post.repository;

import com.example.portfolio_website_backend.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findPostBySlug(String slug);

    List<Post> findPostBySlugStartingWithOrderBySlug(String slug);
}
