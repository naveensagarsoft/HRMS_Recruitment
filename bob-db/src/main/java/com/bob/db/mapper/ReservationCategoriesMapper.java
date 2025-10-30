package com.bob.db.mapper;

import com.bob.db.dto.ReservationCategoriesDTO;
import com.bob.db.entity.ReservationCategoriesEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReservationCategoriesMapper {

    ReservationCategoriesDTO toDto(ReservationCategoriesEntity reservationCategoriesEntity);

    List<ReservationCategoriesDTO> toDtoList(List<ReservationCategoriesEntity> reservationCategoriesEntities);

    ReservationCategoriesEntity toEntity(ReservationCategoriesDTO reservationCategoriesDTO);
    List<ReservationCategoriesEntity> toEntityList(List<ReservationCategoriesDTO> reservationCategoriesDTOS);

}
