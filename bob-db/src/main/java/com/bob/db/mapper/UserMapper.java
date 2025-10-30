package com.bob.db.mapper;

import com.bob.db.dto.UserDTO;
import com.bob.db.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(UserEntity entity);
    UserEntity toEntity(UserDTO dto);
}
