package com.bob.db.repository;

import com.bob.db.dto.LocationDTO;
import com.bob.db.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity,Long> {
    @Query(value = "SELECT dd.location_id,dd.location_name,dd.city_id FROM locations dd", nativeQuery = true)
    List<LocationDTO> getData();

    @Query("SELECT l.cityId FROM LocationEntity l WHERE l.locationId = ?1")
    Long findcityIdByLocationId(Long locationId);


    LocationEntity findByLocationId(Long locationId);

    List<LocationEntity> findAllByIsActiveTrue();

    List<LocationEntity> findAllByLocationIdInAndIsActiveTrue(List<Long> locationIds);


}
