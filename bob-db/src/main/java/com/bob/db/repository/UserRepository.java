package com.bob.db.repository;

import com.bob.db.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findAllByRole(String role);

    Optional<UserEntity> findByOathUserId(String oathUserId);


    List<UserEntity> findAllByIsActiveTrue();

    List<UserEntity> findAllByUserIdInAndIsActiveTrue(List<Long> userIds);
}