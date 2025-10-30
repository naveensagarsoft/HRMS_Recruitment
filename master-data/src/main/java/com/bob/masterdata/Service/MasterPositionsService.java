package com.bob.masterdata.Service;

import com.bob.db.dto.MasterPositionsDTO;
import com.bob.db.entity.JobGradeEntity;
import com.bob.db.entity.MasterPositionsEntity;
import com.bob.db.mapper.MasterPositionsMapper;
import com.bob.db.repository.JobGradeRepository;
import com.bob.db.repository.MasterPositionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MasterPositionsService  {

    @Autowired
    private MasterPositionsRepository masterPositionsRepository;

    @Autowired
    private JobGradeRepository jobGradeRepository;

    @Autowired
    private MasterPositionsMapper masterPositionsMapper;

    @Transactional
    public MasterPositionsDTO create(MasterPositionsDTO dto) {
        MasterPositionsEntity entity = masterPositionsMapper.toEntity(dto);
        entity.setJobGrade(resolveJobGrade(dto.getJobGradeId()));
        entity.setPositionCode(generateNextPositionCode());
        entity.setCreatedBy(dto.getCreatedBy()!=null?dto.getCreatedBy():"1");
        entity.setIsActive(true);
        MasterPositionsEntity saved = masterPositionsRepository.save(entity);
        return masterPositionsMapper.toDTO(saved);
    }

    @Transactional
    public MasterPositionsDTO update(Long id, MasterPositionsDTO dto) {
        Optional<MasterPositionsEntity> opt = masterPositionsRepository.findById(id);
        if (opt.isEmpty()) throw new RuntimeException("MasterPosition not found");
        MasterPositionsEntity entity = opt.get();
        entity.setPositionName(dto.getPositionName());
        entity.setPositionDescription(dto.getPositionDescription());
        entity.setDeptId(dto.getDeptId());
        entity.setJobGrade(resolveJobGrade(dto.getJobGradeId()));
        entity.setUpdatedBy(dto.getUpdatedBy());
        MasterPositionsEntity saved = masterPositionsRepository.save(entity);
        return masterPositionsMapper.toDTO(saved);
    }

    public List<MasterPositionsDTO> getAll() {
        return masterPositionsRepository.findByIsActiveTrue().stream().map(masterPositionsMapper::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
//        masterPositionsRepository.deleteById(id);
        Optional<MasterPositionsEntity> optionalMasterPositions=masterPositionsRepository.findById(id);
        if(optionalMasterPositions.isPresent()){
            MasterPositionsEntity masterPositionsEntity=optionalMasterPositions.get();
            masterPositionsEntity.setIsActive(false);
            masterPositionsRepository.save(masterPositionsEntity);
        }
    }

    private JobGradeEntity resolveJobGrade(Long jobGradeId) {
        if (jobGradeId == null) return null;
        return jobGradeRepository.findById(jobGradeId).orElse(null);
    }

    private String generateNextPositionCode() {
        String latestCode = masterPositionsRepository.findLatestPositionCode();
        if (latestCode == null || !latestCode.startsWith("JPOS")) {
            return "JPOS5001";
        }
        try {
            int num = Integer.parseInt(latestCode.substring(4));
            return "JPOS" + (num + 1);
        } catch (NumberFormatException e) {
            return "JPOS5001";
        }
    }
}
