package com.bob.db.mapper;

import com.bob.db.dto.EducationalQualificationsDTO;
import com.bob.db.entity.EducationQualificationsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EducationalQualificationsMapper {

    EducationalQualificationsDTO toDto(EducationQualificationsEntity educationalQualificationsEntity);

    EducationQualificationsEntity toEntity(EducationalQualificationsDTO educationalQualificationsDTO);

    List<EducationalQualificationsDTO> toDtoList(List<EducationQualificationsEntity> educationalQualificationsEntities);

    List<EducationQualificationsEntity> toEntityList(List<EducationalQualificationsDTO> educationalQualificationsDTOS);
}
