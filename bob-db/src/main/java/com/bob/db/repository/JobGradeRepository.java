package com.bob.db.repository;

import com.bob.db.entity.JobGradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface JobGradeRepository extends JpaRepository<JobGradeEntity,Long> {

    @Query(value = "SELECT dd.job_grade_id,dd.job_scale,dd.min_salary,max_salary FROM job_grade dd where is_active=true", nativeQuery = true)
    List<Map<Long,String>> getData();

    List<JobGradeEntity> findAllByIsActiveTrue();


}
