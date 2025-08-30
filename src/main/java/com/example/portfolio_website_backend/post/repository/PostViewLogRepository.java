package com.example.portfolio_website_backend.post.repository;

import com.example.portfolio_website_backend.post.domain.Post;
import com.example.portfolio_website_backend.post.domain.PostViewLog;
import com.example.portfolio_website_backend.post.dto.response.DailyViewStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface PostViewLogRepository extends JpaRepository<PostViewLog, Long> {
    @Query("SELECT new com.example.portfolio_website_backend.post.dto.response.DailyViewStats(pvl.post, COUNT(pvl), COUNT(DISTINCT pvl.ipAddress)) " +
            "FROM Post p " +
            "LEFT JOIN PostViewLog pvl ON pvl.post = p AND pvl.viewedAt BETWEEN :startDate AND :endDate " +
            "GROUP BY pvl.post")
    List<DailyViewStats> getPostViewLogsByViewedAtRange(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    boolean existsByPostAndIpAddressAndViewedAtAfter(Post post, String ipAddress, Instant after);

}
