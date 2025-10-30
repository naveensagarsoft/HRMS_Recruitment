package com.bob.db.mapper;

import com.bob.db.dto.InterviewPanelsDTO;
import com.bob.db.entity.InterviewPanelsEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface InterviewPanelsMapper {

    InterviewPanelsDTO toDto(InterviewPanelsEntity interviewPanelsEntity);
    InterviewPanelsEntity toEntity(InterviewPanelsDTO interviewPanelsDTO);
    List<InterviewPanelsDTO> toDtoList(List<InterviewPanelsEntity> interviewPanelsEntities);
    List<InterviewPanelsEntity> toEntityList(List<InterviewPanelsDTO> interviewPanelsDTOS);
}
