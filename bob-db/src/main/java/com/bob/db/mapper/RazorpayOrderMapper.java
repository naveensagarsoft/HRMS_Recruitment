package com.bob.db.mapper;

import com.bob.db.dto.RazorpayOrderDTO;
import com.bob.db.entity.RazorpayOrderEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RazorpayOrderMapper {

    RazorpayOrderDTO toDto(RazorpayOrderEntity entity);

    RazorpayOrderEntity toEntity(RazorpayOrderDTO dto);

    List<RazorpayOrderDTO> toDtoList(List<RazorpayOrderEntity> entities);

    List<RazorpayOrderEntity> toEntityList(List<RazorpayOrderDTO> dtos);
}
