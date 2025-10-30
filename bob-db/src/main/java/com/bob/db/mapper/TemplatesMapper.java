package com.bob.db.mapper;

import com.bob.db.dto.TemplatesDTO;
import com.bob.db.entity.TemplatesEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface TemplatesMapper {

    TemplatesDTO toDto(TemplatesEntity templatesEntity);

    List<TemplatesDTO> toDtoList(List<TemplatesEntity> templatesEntities);
}
