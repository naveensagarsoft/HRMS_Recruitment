package com.bob.db.mapper;

import com.bob.db.dto.InterviewPanelMembersDTO;
import com.bob.db.entity.InterviewPanelMembersEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface InterviewPanelMembersMapper {

    InterviewPanelMembersDTO toDto(InterviewPanelMembersEntity interviewPanelMembersEntity);
    InterviewPanelMembersEntity toEntity(InterviewPanelMembersDTO interviewPanelMembersDTO);

    List<InterviewPanelMembersDTO> toDtoList(List<InterviewPanelMembersEntity> interviewPanelMembersEntities);

    List<InterviewPanelMembersEntity> toEntityList(List<InterviewPanelMembersDTO> interviewPanelMembersDTOS);
}
