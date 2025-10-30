package com.bob.masterdata.Service;

import com.bob.db.dto.UserDTO;
import com.bob.db.entity.UserEntity;
import com.bob.db.mapper.UserMapper;
import com.bob.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public UserDTO createUser(UserDTO userDTO) throws Exception {
        try {
            UserEntity saved = userRepository.save(userMapper.toEntity(userDTO));
            return userMapper.toDTO(saved);
        } catch (Exception e) {
            throw new Exception("Faile" + e.getMessage());
        }
    }

    public List<UserDTO> getAllUsers() throws Exception {
        try {
            List<UserEntity> entities = userRepository.findAllByIsActiveTrue();
            return entities.stream().map(userMapper::toDTO).toList();
        } catch (Exception e) {
            throw new Exception("Failed to fetch user");
        }
    }

    public UserDTO updateUser(long id, UserDTO userDTO) {
        try {
            Optional<UserEntity> existingUser = userRepository.findById(id);
            if (existingUser.isPresent()) {
                UserEntity entity = userMapper.toEntity(userDTO);
                entity.setUserId(id);
                entity.setUpdatedDate(LocalDateTime.now());
                UserEntity saved = userRepository.save(entity);
                return userMapper.toDTO(saved);
            } else {
                throw new Exception("ID DOESN'T EXIST!");
            }
        } catch (Exception e) {
            return null;
        }
    }

    public UserDTO deleteUser(long id) {
        try {
            Optional<UserEntity> entityOpt = userRepository.findById(id);
            if (entityOpt.isPresent()) {
                UserEntity entity = entityOpt.get();
                entity.setIsActive(false);
                userRepository.save(entity);
//                userRepository.deleteById(id);
                return userMapper.toDTO(entity);
            } else {
                throw new Exception("ID DOESN'T EXIST");
            }
        } catch (Exception e) {
            return null;
        }
    }
}
