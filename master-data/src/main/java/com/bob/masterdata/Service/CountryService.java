package com.bob.masterdata.Service;

import com.bob.db.dto.CountryDTO;
import com.bob.db.entity.CountryEntity;
import com.bob.db.mapper.CountryMapper;
import com.bob.db.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CountryService {

    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private CountryMapper countryMapper;

    public CountryDTO createCountry(CountryDTO country) {
        try {
            CountryEntity countryEntity=countryMapper.toEntity(country);
            countryRepository.save(countryEntity);
            return countryMapper.toDto(countryEntity);
        } catch (Exception e) {
            return null;
        }
    }

    public List<CountryDTO> getAllCountries() throws Exception {
        try {
            return countryMapper.toDtoList(countryRepository.findAllByIsActiveTrue());
        } catch (Exception e) {
            throw new Exception("Failed to fetch countries");
        }
    }

    public CountryDTO updateCountry(Long id, CountryDTO country) {
        try {
            Optional<CountryEntity> existingCountry = countryRepository.findById(id);
            if (existingCountry.isPresent()) {
                CountryEntity countryEntity = countryMapper.toEntity(country);
                countryEntity.setCountryId(id);
                countryEntity.setCountryName(country.getCountryName());
                countryEntity.setCreatedBy(country.getCreatedBy());
                countryRepository.save(countryEntity);
                return countryMapper.toDto(countryEntity);
            } else {
                throw new Exception("ID DOESN'T EXIST!");
            }
        } catch (Exception e) {
            return null;
        }
    }

    public CountryDTO deleteCountry(Long id) {
        try {
            if (countryRepository.existsById(id)) {
                CountryEntity countryEntity=countryRepository.findById(id).get();
                countryEntity.setIsActive(false);
                countryRepository.save(countryEntity);
                CountryDTO country=countryMapper.toDto(countryEntity);
//                countryRepository.deleteById(id);
                return country;
            } else {
                throw new Exception("ID DOESN'T EXIST");
            }
        } catch (Exception e) {
            return null;
        }
    }
}
