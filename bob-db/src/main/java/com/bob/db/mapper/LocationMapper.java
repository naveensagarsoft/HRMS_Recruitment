package com.bob.db.mapper;

import com.bob.db.dto.LocationDTO;
import com.bob.db.entity.LocationEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface LocationMapper {

    LocationDTO toDto(LocationEntity locationEntity);

    List<LocationDTO> toDtoList(List<LocationEntity> locationEntities);

    LocationEntity toEntity(LocationDTO locationDTO);

    List<LocationEntity> toEntityList(List<LocationDTO> locationDTOS);
}

