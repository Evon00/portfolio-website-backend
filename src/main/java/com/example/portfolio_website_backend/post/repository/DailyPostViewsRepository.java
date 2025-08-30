package com.example.portfolio_website_backend.post.repository;

import com.example.portfolio_website_backend.post.domain.DailyPostViews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyPostViewsRepository extends JpaRepository<DailyPostViews, Long> {
}
