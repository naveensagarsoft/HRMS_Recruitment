package com.bob.db.mapper;

import com.bob.db.dto.BulkResumeUploadDto;
import com.bob.db.entity.BulkResumeUploadEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BulkResumeUploadMapper {
    BulkResumeUploadDto toDTO(BulkResumeUploadEntity entity);
    BulkResumeUploadEntity toEntity(BulkResumeUploadDto dto);
}
