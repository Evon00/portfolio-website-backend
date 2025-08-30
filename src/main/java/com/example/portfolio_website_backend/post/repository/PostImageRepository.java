package com.example.portfolio_website_backend.post.repository;

import com.example.portfolio_website_backend.post.domain.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {

    List<PostImage> findPostImagesByPostId(Long id);

    @Modifying
    @Query("DELETE FROM PostImage pi WHERE pi.post.id = :id")
    void deletePostImageByPostId(@Param("id") Long id);
}
