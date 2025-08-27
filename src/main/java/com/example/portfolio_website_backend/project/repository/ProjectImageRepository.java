package com.example.portfolio_website_backend.project.repository;

import com.example.portfolio_website_backend.project.domain.ProjectImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectImageRepository extends JpaRepository<ProjectImage, Long> {

    @Query("SELECT pi FROM ProjectImage pi WHERE pi.project.id IN :projectIds ORDER BY pi.project.id, pi.displayOrder")
    List<ProjectImage> findByProjectIdInOrderByDisplayOrder(@Param("projectIds") List<Long> projectIds);

    List<ProjectImage> findByProjectIdOrderByDisplayOrder(Long projectId);

    int countByProjectId(Long projectId);
}
