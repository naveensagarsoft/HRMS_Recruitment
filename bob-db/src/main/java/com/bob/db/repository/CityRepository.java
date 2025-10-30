package com.bob.db.repository;

import com.bob.db.dto.CityDTO;
import com.bob.db.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<CityEntity,Long> {

    @Query(value = "SELECT dd.city_id,dd.city_name,dd.state_id FROM city dd", nativeQuery = true)
    List<CityDTO> getData();

    @Query("SELECT c.stateId FROM CityEntity c WHERE c.cityId = ?1")
    Long findStateIdByCityId(Long cityId);

    List<CityEntity> findAllByIsActiveTrue();

    List<CityEntity> findAllByCityIdInAndIsActiveTrue(Collection<Long> cityIds);
}
