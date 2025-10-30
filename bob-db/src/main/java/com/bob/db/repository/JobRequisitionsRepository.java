package com.bob.db.repository;

import com.bob.db.entity.JobRequisitionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface JobRequisitionsRepository extends JpaRepository<JobRequisitionsEntity, UUID>, JobRequisitionsRepositoryCustom {

    List<JobRequisitionsEntity> findByRequisitionStatus(String status);

    List<JobRequisitionsEntity> findByCreatedBy(String createdBy);

//    List<JobRequisitionsEntity> findAllByRequisitionIdInAndIsactive(Integer requisitionActive, Collection<UUID> requisitionIds);

    // SELECT * FROM job_requisitions WHERE isactive = 1;

    List<JobRequisitionsEntity> findAllByIsactiveOrderByRequisitionCodeDesc(Integer isactive);


    List<JobRequisitionsEntity> findAllByRequisitionStatusAndIsactive(String requisitionStatus, Integer isactive);


    // SELECT * FROM job_requisitions WHERE isactive = ? AND requisition_status IN (?1, ?2)
    List<JobRequisitionsEntity> findAllByIsactiveAndRequisitionStatusIn(Integer isactive, Collection<String> requisitionStatuses);


    List<JobRequisitionsEntity> findAllByRequisitionIdInAndIsactiveOrderByRequisitionCodeDesc(List<UUID> requisitionIds, Integer isactive);

}
