package com.bob.db.repository;

import com.bob.db.entity.DepartmentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DepartmentsRepository extends JpaRepository<DepartmentsEntity, Long> {

    @Query(value = "SELECT d.department_name FROM departments d where d.isActive=true", nativeQuery = true)
    List<String> getDepartmentNames();

    @Query(value = "SELECT dd.department_id,dd.department_name FROM departments dd where dd.isActive=true", nativeQuery = true)
    List<Map<Long,String>> getData();

    List<DepartmentsEntity> findAllByIsActiveTrue();
}
