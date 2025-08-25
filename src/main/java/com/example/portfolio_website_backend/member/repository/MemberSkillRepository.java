package com.example.portfolio_website_backend.member.repository;

import com.example.portfolio_website_backend.member.domain.Member;
import com.example.portfolio_website_backend.member.domain.MemberSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberSkillRepository extends JpaRepository<MemberSkill, Long> {

    List<MemberSkill> findByMember(Member member);

    @Modifying
    @Query("DELETE FROM MemberSkill ms WHERE ms.member = :member")
    void deleteByMember(Member member);
}
