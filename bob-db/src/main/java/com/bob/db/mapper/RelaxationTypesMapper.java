package com.bob.db.mapper;

import com.bob.db.dto.RelaxationTypesDTO;
import com.bob.db.entity.RelaxationTypesEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RelaxationTypesMapper {
    RelaxationTypesDTO toDTO(RelaxationTypesEntity relaxationTypeEntity);
    RelaxationTypesEntity toEntity(RelaxationTypesDTO relaxationTypeDTO);
    List<RelaxationTypesDTO> toDTOList(List<RelaxationTypesEntity> relaxationTypeEntities);
    List<RelaxationTypesEntity> toEntityList(List<RelaxationTypesDTO> relaxationTypeDTOS);
}
