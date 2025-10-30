package com.bob.db.mapper;

import com.bob.db.dto.CountryDTO;
import com.bob.db.entity.CountryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CountryMapper {

    CountryDTO toDto(CountryEntity countryEntity);

    CountryEntity toEntity(CountryDTO countryDto);

    List<CountryDTO> toDtoList(List<CountryEntity> countryEntities);

    List<CountryEntity> toEntityList(List<CountryDTO> countryDTOS);

}
