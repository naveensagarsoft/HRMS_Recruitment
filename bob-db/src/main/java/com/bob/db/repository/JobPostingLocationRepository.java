package com.bob.db.repository;

import com.bob.db.entity.JobPostingLocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JobPostingLocationRepository extends JpaRepository<JobPostingLocationEntity, Long> {

    @Query(value = "SELECT jl.location_id FROM job_posting_location jl WHERE jl.position_id = :positionId", nativeQuery = true)
    List<Long> findByPositionIdNative(@Param("positionId") UUID positionId);

    JobPostingLocationEntity findByPositionId(UUID positionId);

    List<JobPostingLocationEntity> findByPositionIdIn(List<UUID> positionIds);

}
