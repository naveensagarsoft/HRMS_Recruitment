package com.bob.masterdata.Service;

import com.bob.db.dto.CityDTO;
import com.bob.db.dto.LocationDTO;
import com.bob.db.dto.MasterPositionsDTO;
import com.bob.db.dto.StateDTO;
import com.bob.db.entity.ReservationCategoriesEntity;
import com.bob.db.mapper.CityMapper;
import com.bob.db.mapper.LocationMapper;
import com.bob.db.mapper.MasterPositionsMapper;
import com.bob.db.mapper.StateMapper;
import com.bob.db.repository.*;
import com.bob.masterdata.Model.GetCompleteDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DisplayService {

    @Autowired
    private ReservationCategoriesRepository reservationCategories;
    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private DepartmentsRepository departmentsRepository;

    @Autowired
    private EducationalQualificationsRepository educationalQualificationsRepository;

    @Autowired
    private StateMapper stateMapper;

    @Autowired
    private LocationMapper locationMapper;

    @Autowired
    private CityMapper cityMapper;
    @Autowired
    private JobGradeRepository jobGradeRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private SpecialCategoriesRepository specialCategoriesRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private MasterPositionsRepository masterPositionsRepository;

    @Autowired
    private MasterPositionsMapper masterPositionsMapper;

    public List<ReservationCategoriesEntity> getAllCategories(){
        return reservationCategories.findAll();
    }

    public Map<String,List<?>> getAllData(){
        Map<String,List<?>> mp=new HashMap<>();
        mp.put("City", cityRepository.findAll());
        mp.put("Country",countryRepository.findAll());
        mp.put("DepartmentsEntity",departmentsRepository.findAll());
        mp.put("Educational Qualifications",educationalQualificationsRepository.findAll());
        mp.put("Job Grade",jobGradeRepository.findAll());
        mp.put("Location",locationRepository.findAll());
        mp.put("Reservation Categories",reservationCategories.findAll());
        mp.put("Skills", skillRepository.findAll());
        mp.put("Special Categories",specialCategoriesRepository.findAll());
        mp.put("State",stateRepository.findAll());
        return mp;
    }



    public GetCompleteDataResponse getAllData3(){
        List<String> position_title=departmentsRepository.getDepartmentNames();
        List<Map<Long,String>> department=departmentsRepository.getData();

        List<Map<Long,String>> countries=countryRepository.getData();
        List<StateDTO> states=stateMapper.toDtoList(stateRepository.findAll().stream().
                filter(stateEntity -> stateEntity.getIsActive().equals(true)).toList());

        List<CityDTO> cities=cityMapper.toDtoList(cityRepository.findAll().stream().
                filter(cityEntity -> cityEntity.getIsActive().equals(true)).toList());

        List<LocationDTO> locations=locationMapper.toDtoList(locationRepository.findAllByIsActiveTrue());
        List<Map<Long,String>> skills=skillRepository.getSkillIdDescriptions();
        List<Map<Long,String>> job_Data=jobGradeRepository.getData();
        List<Map<Long,String>> mandatory= educationalQualificationsRepository.getData();
        List<Map<Long,String>> preferred= educationalQualificationsRepository.getData();

        List<MasterPositionsDTO> masterPositionsDTOS = masterPositionsMapper.toDTOList(masterPositionsRepository.findByIsActiveTrue());
        GetCompleteDataResponse completeDataResponse=new GetCompleteDataResponse();
        completeDataResponse.setPositionTitle(position_title);
        completeDataResponse.setDepartments(department);
        completeDataResponse.setCountries(countries);
        completeDataResponse.setStates(states);
        completeDataResponse.setCities(cities);
        completeDataResponse.setLocations(locations);
        completeDataResponse.setSkills(skills);
        completeDataResponse.setJobGradeData(job_Data);
        completeDataResponse.setMandatoryQualification(mandatory);
        completeDataResponse.setPreferredQualification(preferred);
        completeDataResponse.setMasterPositionsList(masterPositionsDTOS);
        return completeDataResponse;
    }


}
