package com.example.portfolio_website_backend.project.repository;

import com.example.portfolio_website_backend.project.domain.Project;
import com.example.portfolio_website_backend.project.domain.ProjectSkill;
import com.example.portfolio_website_backend.stats.dto.response.PopularSkillResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface ProjectSkillRepository extends JpaRepository<ProjectSkill, Long> {

    @Query("""
            SELECT new com.example.portfolio_website_backend.stats.dto.response.PopularSkillResponseDTO(s.skillName, COUNT(ps))
            FROM ProjectSkill ps
            JOIN ps.skill s
            GROUP BY s.skillName
            ORDER BY COUNT(ps) DESC
            """)
    List<PopularSkillResponseDTO> findPopularSkills(Pageable pageable);

    @Modifying
    @Query("DELETE FROM ProjectSkill ps WHERE ps.project = :project")
    void deleteByProject(Project project);
}
