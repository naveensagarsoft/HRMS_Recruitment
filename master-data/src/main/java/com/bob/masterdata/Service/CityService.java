package com.bob.masterdata.Service;

import com.bob.db.dto.CityDTO;
import com.bob.db.entity.CityEntity;
import com.bob.db.mapper.CityMapper;
import com.bob.db.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CityService {
    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CityMapper cityMapper;

    public CityDTO createCity(CityDTO city) {
        try {
            cityRepository.save(cityMapper.toEntity(city));
            return city;
        } catch (Exception e) {
            return null;
        }
    }

    public List<CityDTO> getAllCities() throws Exception {
        try {
            return cityMapper.toDtoList(cityRepository.findAllByIsActiveTrue());
        } catch (Exception e) {
            throw new Exception("Failed to fetch cities");
        }
    }

    public CityDTO updateCities(Long id, CityDTO city) {
        try {
            CityDTO replica=city;
            Optional<CityEntity> existingCity = cityRepository.findById(id);
            if (existingCity.isPresent()) {
                city.setCityId(id);
                cityRepository.save(cityMapper.toEntity(city));
                return replica;
            } else {
                throw new Exception("ID DOESN'T EXIST!");
            }
        } catch (Exception e) {
            return null;
        }
    }

    public CityDTO deleteCities(Long id) {
        try {
            if (cityRepository.existsById(id)) {
                CityEntity cityEntity=cityRepository.findById(id).get();
                cityEntity.setIsActive(false);
                cityRepository.save(cityEntity);
                CityDTO city=cityMapper.toDto(cityEntity);
//                cityRepository.deleteById(id);
                return city;
            } else {
                throw new Exception("ID DOESN'T EXIST");
            }
        } catch (Exception e) {
            return null;
        }
    }
}
