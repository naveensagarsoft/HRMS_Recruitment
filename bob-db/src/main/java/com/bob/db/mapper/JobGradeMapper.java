package com.bob.db.mapper;

import com.bob.db.dto.JobGradeDTO;
import com.bob.db.entity.JobGradeEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface JobGradeMapper {

    JobGradeDTO toDto(JobGradeEntity jobGradeEntity);

    List<JobGradeDTO> toDtoList(List<JobGradeEntity> jobGradeEntities);

    JobGradeEntity toEntity(JobGradeDTO jobGradeDTO);

    List<JobGradeEntity> toEntityList(List<JobGradeDTO> jobGradeDTOS);
}
