package com.bob.masterdata.Service;


import com.bob.db.dto.RelaxationTypesDTO;
import com.bob.db.entity.RelaxationTypesEntity;
import com.bob.db.mapper.RelaxationTypesMapper;
import com.bob.db.repository.RelaxationTypesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelaxationTypeService {

    @Autowired
    private RelaxationTypesRepository relaxationTypesRepository;

    @Autowired
    private RelaxationTypesMapper relaxationTypesMapper;
    public RelaxationTypesDTO addRelaxationType(RelaxationTypesDTO dto) {
        RelaxationTypesEntity relaxationTypeEntity = relaxationTypesMapper.toEntity(dto);
        return relaxationTypesMapper.toDTO(relaxationTypesRepository.save(relaxationTypeEntity));
    }

    public RelaxationTypesDTO updateRelaxationType(Integer id,RelaxationTypesDTO dto) {
        RelaxationTypesEntity existingEntity = relaxationTypesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Relaxation Type not found with id: " + id));
        existingEntity.setRelaxationTypeName(dto.getRelaxationTypeName());
        existingEntity.setDescription(dto.getDescription());
        existingEntity.setOthers(dto.getOthers());
        existingEntity.setInput(dto.getInput());
        existingEntity.setOperator(dto.getOperator());
        return relaxationTypesMapper.toDTO(relaxationTypesRepository.save(existingEntity));
    }

    public List<RelaxationTypesDTO> getAllRelaxationTypes() {
        List<RelaxationTypesEntity> entities = relaxationTypesRepository.findAllByIsActiveTrue();
        return relaxationTypesMapper.toDTOList(entities);
    }

    public void deleteRelaxationType(Integer id) {
        RelaxationTypesEntity existingEntity = relaxationTypesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Relaxation Type not found with id: " + id));
        existingEntity.setIsActive(false);
        relaxationTypesRepository.save(existingEntity);
    }
}
