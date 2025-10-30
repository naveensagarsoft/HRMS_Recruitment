package com.bob.db.mapper;

import com.bob.db.dto.InterviewersDTO;
import com.bob.db.entity.InterviewersEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface InterviewersMapper {
    InterviewersDTO toDto(InterviewersEntity interviewersEntity);
    InterviewersEntity toEntity(InterviewersDTO interviewersDTO);
    List<InterviewersDTO> toDtoList(List<InterviewersEntity> interviewersEntities);

    List<InterviewersEntity> toEntityList(List<InterviewersDTO> interviewersDTOS);
}
