package com.bob.masterdata.Service;

import com.bob.db.dto.LocationDTO;
import com.bob.db.entity.LocationEntity;
import com.bob.db.mapper.LocationMapper;
import com.bob.db.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {
    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationMapper locationMapper;

    public LocationDTO createLocation(LocationDTO location) {
        try {
            LocationEntity locationEntity=locationMapper.toEntity(location);
            locationRepository.save(locationEntity);
            return location;
        } catch (Exception e) {
            return null;
        }
    }

    public List<LocationDTO> getAllLocations() throws Exception {
        try {
            return locationMapper.toDtoList(locationRepository.findAllByIsActiveTrue());
        } catch (Exception e) {
            throw new Exception("Failed to fetch locations");
        }
    }

    public LocationDTO updateLocations(Long id,LocationDTO location) {
        try {
//            LocationEntity location1=location;
            Optional<LocationEntity> existingLocation = locationRepository.findById(id);
            if (existingLocation.isPresent()) {
                LocationEntity locationEntity=existingLocation.get();
                locationEntity.setLocationId(id);
                locationEntity.setLocationName(location.getLocationName());
                locationEntity.setCityId(location.getCityId());
                locationRepository.save(locationEntity);
                return locationMapper.toDto(locationEntity);
            } else {
                throw new Exception("ID DOESN'T EXIST!");
            }
        } catch (Exception e) {
            return null;
        }
    }

    public LocationDTO deleteLocation(Long id) {
        try {
            if (locationRepository.existsById(id)) {
                LocationEntity location=locationRepository.findById(id).get();
                location.setIsActive(false);
                locationRepository.save(location);
//                locationRepository.deleteById(id);
                return locationMapper.toDto(location);
            } else {
                throw new Exception("ID DOESN'T EXIST");
            }
        } catch (Exception e) {
            return null;
        }
    }
}
