package com.bob.db.mapper;

import com.bob.db.dto.DocumentTypesDTO;
import com.bob.db.entity.DocumentTypesEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DocumentTypesMapper {
    DocumentTypesEntity toEntity(DocumentTypesDTO documentTypesDTO);
    DocumentTypesDTO toDTO(DocumentTypesEntity documentTypesEntity);
    List<DocumentTypesDTO> toDTOList(List<DocumentTypesEntity> documentTypesEntities);
    List<DocumentTypesEntity> toEntityList(List<DocumentTypesDTO> documentTypesDTOS);
}
