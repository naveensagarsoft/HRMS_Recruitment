package com.bob.db.repository;

import com.bob.db.entity.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Repository
public interface CountryRepository extends JpaRepository<CountryEntity, Long> {

    @Query(value = "SELECT dd.country_id,dd.country_name FROM country dd where dd.is_active=true", nativeQuery = true)
    List<Map<Long,String>> getData();


    List<CountryEntity> findAllByIsActiveTrue();


    List<CountryEntity> findAllByCountryIdInAndIsActiveTrue(Collection<Long> countryIds);
}
