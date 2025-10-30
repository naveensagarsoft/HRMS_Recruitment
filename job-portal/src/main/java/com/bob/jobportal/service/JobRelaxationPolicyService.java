package com.bob.jobportal.service;

import com.bob.db.dto.JobRelaxationPolicyDTO;
import com.bob.db.entity.JobRelaxationPolicyEntity;
import com.bob.db.entity.JobRequisitionsEntity;
import com.bob.db.entity.PositionsEntity;
import com.bob.db.mapper.JobRelaxationPolicyMapper;
import com.bob.db.repository.JobRelaxationPolicyRepository;
import com.bob.db.repository.JobRequisitionsRepository;
import com.bob.db.repository.PositionsRepository;
import com.bob.jobportal.util.AppConstants;
import com.bob.jobportal.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class JobRelaxationPolicyService {
    @Autowired
    private JobRelaxationPolicyRepository jobRelaxationPolicyRepository;

    @Autowired
    private JobRelaxationPolicyMapper jobRelaxationPolicyMapper;

    @Autowired
    private PositionsRepository positionsRepository;

    @Autowired
    private JobRequisitionsRepository jobRequisitionsRepository;

    @Autowired
    private SecurityUtils securityUtils;

    public JobRelaxationPolicyDTO addRelaxationPolicy(JobRelaxationPolicyDTO dto) {
        JobRelaxationPolicyEntity entity = jobRelaxationPolicyMapper.toEntity(dto);
        entity.setRelaxationPolicyNumber("RP-"+String.format("%04d", jobRelaxationPolicyRepository.count() + 1));
        return jobRelaxationPolicyMapper.toDTO(jobRelaxationPolicyRepository.save(entity));
    }

    public List<JobRelaxationPolicyDTO> getAllRelaxationPolicies() {
        List<JobRelaxationPolicyEntity> entities = jobRelaxationPolicyRepository.findAllByOrderByCreatedDateDesc();
        return jobRelaxationPolicyMapper.toDTOList(entities);
    }

    public JobRelaxationPolicyDTO updateRelaxationPolicy(UUID id, JobRelaxationPolicyDTO policy) {
        JobRelaxationPolicyEntity existingPolicy = jobRelaxationPolicyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy not found"));

        List< PositionsEntity> positions = positionsRepository.findByJobRelaxationPolicyId(id);
        List< UUID> requisitionIds = positions.stream()
                .map(PositionsEntity::getRequisitionId)
                .distinct()
                .toList();

        List<JobRequisitionsEntity> requisitionsEntities = jobRequisitionsRepository.findAllById(requisitionIds);
        List<JobRequisitionsEntity> filter = requisitionsEntities.stream().filter(req -> !req.getRequisitionStatus()
                .equals(AppConstants.REQ_APPROVAL_New)).toList();

        if(!filter.isEmpty()){
            throw new RuntimeException("Cannot update policy linked with active requisitions");
        }
        if( !existingPolicy.getCreatedBy().equals(securityUtils.getCurrentUserToken())){
            throw new RuntimeException("Only creator can update the policy");
        }
        existingPolicy.setRelaxation(policy.getRelaxation());
//        existingPolicy.setPositionId(policy.getPositionId());
        jobRelaxationPolicyMapper.toDTO(jobRelaxationPolicyRepository.save(existingPolicy));
        return policy;
    }

    public JobRelaxationPolicyDTO getRelaxationPolicyById(UUID id) {
        JobRelaxationPolicyEntity entity = jobRelaxationPolicyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy not found"));
        return jobRelaxationPolicyMapper.toDTO(entity);
    }
}
