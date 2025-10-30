package com.bob.db.mapper;

import com.bob.db.dto.JobVacancyDto;
import com.bob.db.entity.JobVacanciesEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JobVacanciesMapper {

    JobVacancyDto toDto(JobVacanciesEntity entity);

    JobVacanciesEntity toEntity(JobVacancyDto dto);

    List<JobVacancyDto> toDtoList(List<JobVacanciesEntity> entities);

    List<JobVacanciesEntity> toEntityList(List<JobVacancyDto> dtos);
}
