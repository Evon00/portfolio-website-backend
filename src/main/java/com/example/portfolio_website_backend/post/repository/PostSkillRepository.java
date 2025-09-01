package com.example.portfolio_website_backend.post.repository;

import com.example.portfolio_website_backend.post.domain.Post;
import com.example.portfolio_website_backend.post.domain.PostSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostSkillRepository extends JpaRepository<PostSkill, Long> {

    @Modifying
    @Query("DELETE FROM PostSkill ps WHERE ps.post = :post")
    void deleteByPost(Post post);
}
