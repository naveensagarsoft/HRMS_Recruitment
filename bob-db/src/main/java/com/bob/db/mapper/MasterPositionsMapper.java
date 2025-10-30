package com.bob.db.mapper;

import com.bob.db.dto.MasterPositionsDTO;
import com.bob.db.entity.MasterPositionsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MasterPositionsMapper {
    MasterPositionsMapper INSTANCE = Mappers.getMapper(MasterPositionsMapper.class);

    @Mapping(target = "jobGradeId", source = "jobGrade.jobGradeId")
    MasterPositionsDTO toDTO(MasterPositionsEntity entity);

    @Mapping(target = "jobGrade", ignore = true)
    MasterPositionsEntity toEntity(MasterPositionsDTO dto);

    List<MasterPositionsDTO> toDTOList(List<MasterPositionsEntity> entities);

    List<MasterPositionsEntity> toEntityList(List<MasterPositionsDTO> dtos);
}
