package com.bob.db.repository;

import com.bob.db.entity.InterviewPanelsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewPanelsRepository extends JpaRepository<InterviewPanelsEntity, Long> {
    List<InterviewPanelsEntity> findAllByIsActiveTrue();
    Optional<InterviewPanelsEntity> findByPanelIdAndIsActiveTrue(Long panelId);
    List<InterviewPanelsEntity> findAllByPanelIdInAndIsActiveTrue(List<Long> panelIds);
}
