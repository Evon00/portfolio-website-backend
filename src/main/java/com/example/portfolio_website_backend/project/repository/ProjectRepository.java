package com.example.portfolio_website_backend.project.repository;

import com.example.portfolio_website_backend.project.domain.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @EntityGraph(attributePaths = {"projectSkills.skill"})
    Page<Project> findAll(Pageable pageable);

    List<Project> findProjectsBySlugStartingWith(String slug);

    Optional<Project> findProjectBySlug(String slug);
}
