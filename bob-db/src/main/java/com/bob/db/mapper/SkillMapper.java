package com.bob.db.mapper;

import com.bob.db.dto.SkillDTO;
import com.bob.db.entity.SkillEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SkillMapper {


    SkillDTO toDto(SkillEntity skillEntity);

    List<SkillDTO> toDtoList(List<SkillEntity> skillEntities);

    SkillEntity toEntity(SkillDTO skillDto);

    List<SkillEntity> toEntityList(List<SkillDTO> skillDTOS);
}
