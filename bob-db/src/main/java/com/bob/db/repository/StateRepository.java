package com.bob.db.repository;

import com.bob.db.dto.StateDTO;
import com.bob.db.entity.StateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface StateRepository extends JpaRepository<StateEntity,Long> {
    @Query(value = "SELECT dd.state_id,dd.state_name,dd.country_id FROM state dd", nativeQuery = true)
    List<StateDTO> getData();

    @Query(value = "SELECT * FROM state WHERE city_id = :cityId", nativeQuery = true)
    List<StateEntity> findByCityId(@Param("cityId") Integer cityId);


    StateEntity findByStateId(Long stateId);

    List<StateEntity> findAllByIsActiveTrue();


    List<StateEntity> findAllByStateIdInAndIsActiveTrue(Collection<Long> stateIds);

}
