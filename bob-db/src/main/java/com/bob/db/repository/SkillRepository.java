package com.bob.db.repository;

import com.bob.db.entity.SkillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SkillRepository extends JpaRepository<SkillEntity,Long> {

    @Query(value = "SELECT d.skill_id,d.skill_desc FROM skill d where d.is_active=true", nativeQuery = true)
    List<Map<Long,String>> getSkillIdDescriptions();


    List<SkillEntity> findAllByIsActiveTrue();
}
