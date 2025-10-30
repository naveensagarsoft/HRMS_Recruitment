package com.bob.db.repository;

import com.bob.db.entity.EducationQualificationsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface EducationalQualificationsRepository extends JpaRepository<EducationQualificationsEntity,Long> {

    @Query(value = "SELECT dd.edu_qualification_id,dd.edu_qualification_name FROM education_qualifications dd where dd.is_active=true", nativeQuery = true)
    List<Map<Long,String>> getData();

    List<EducationQualificationsEntity> findAllByIsActiveTrue();
}
