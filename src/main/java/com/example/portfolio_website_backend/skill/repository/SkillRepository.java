package com.example.portfolio_website_backend.skill.repository;

import com.example.portfolio_website_backend.skill.domain.Skill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    Page<Skill> findAllByOrderById(Pageable pageable);

    Page<Skill> findByCategoryOrderById(String category, Pageable pageable);

    @Query("SELECT DISTINCT s.category FROM Skill s ORDER BY s.category")
    List<String> findDistinctByCategories();

    List<Skill> findSkillsBySkillNameStartingWithIgnoreCaseOrderBySkillNameAsc(String skillName);
}
