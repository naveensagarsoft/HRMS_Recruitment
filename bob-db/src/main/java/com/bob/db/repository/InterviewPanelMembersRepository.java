package com.bob.db.repository;

import com.bob.db.entity.InterviewPanelMembersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;
@Repository
public interface InterviewPanelMembersRepository extends JpaRepository<InterviewPanelMembersEntity, Long> {
    List<InterviewPanelMembersEntity> findAllByPanelId(Long panelId);

    // Delete all members for one panel
    void deleteAllByPanelId(Long panelId);

    // Get all members for multiple panels
    List<InterviewPanelMembersEntity> findAllByPanelIdIn(List<Long> panelIds);
}
