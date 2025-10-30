package com.bob.db.mapper;

import com.bob.db.dto.DepartmentsDTO;
import com.bob.db.entity.DepartmentsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface DepartmentsMapper {

    DepartmentsDTO toDto(DepartmentsEntity departmentsEntity);

    DepartmentsEntity toEntity(DepartmentsDTO departmentsDTO);

    List<DepartmentsDTO> toDtoList(List<DepartmentsEntity> departmentsEntityList);

    List<DepartmentsEntity> toEntityList(List<DepartmentsDTO> departmentsDTOList);

}
