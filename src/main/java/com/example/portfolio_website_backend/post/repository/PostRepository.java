package com.example.portfolio_website_backend.post.repository;

import com.example.portfolio_website_backend.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("""
           SELECT DISTINCT p FROM Post p
           JOIN p.postSkills ps
           JOIN ps.skill s
           WHERE s.skillName = :skillName
           """)
    Page<Post> findBySkillName(@Param("skillName") String skillName, Pageable pageable);

}
