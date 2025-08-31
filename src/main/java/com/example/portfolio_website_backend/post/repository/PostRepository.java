package com.example.portfolio_website_backend.post.repository;

import com.example.portfolio_website_backend.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findPostBySlug(String slug);

    List<Post> findPostBySlugStartingWithOrderBySlug(String slug);

    @Query("SELECT COALESCE(SUM(p.view), 0) FROM Post p")
    Long getTotalViews();

    List<Post> findTop5ByOrderByCreatedAtDesc();

    List<Post> findTop5ByOrderByViewDesc();
}
