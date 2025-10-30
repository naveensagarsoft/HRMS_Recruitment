package com.bob.masterdata.Service;

import com.bob.db.dto.EducationalQualificationsDTO;
import com.bob.db.entity.EducationQualificationsEntity;
import com.bob.db.mapper.EducationalQualificationsMapper;
import com.bob.db.repository.EducationalQualificationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EducationalQualificationsService {
    @Autowired
    private EducationalQualificationsRepository educationalQualificationsRepository;

    @Autowired
    private EducationalQualificationsMapper educationalQualificationsMapper;


    public EducationalQualificationsDTO createEduQual(EducationalQualificationsDTO educationQualificationsDto) {
        try {
            EducationQualificationsEntity educationQualificationsEntity = educationalQualificationsMapper.toEntity(educationQualificationsDto);
            educationalQualificationsRepository.save(educationQualificationsEntity);
            return educationalQualificationsMapper.toDto(educationQualificationsEntity);
        } catch (Exception e) {
            return null;
        }
    }

    public List<EducationalQualificationsDTO> getAllEduQual() throws Exception {
        try {
            return educationalQualificationsMapper.toDtoList(educationalQualificationsRepository.findAllByIsActiveTrue());
        } catch (Exception e) {
            throw new Exception("Failed to fetch Qualifications");
        }
    }

    public EducationalQualificationsDTO updateEduQual(Long id, EducationalQualificationsDTO educationQualificationsDto) {
        try {
            Optional<EducationQualificationsEntity> existingEdu = educationalQualificationsRepository.findById(id);
            if (existingEdu.isPresent()) {
                EducationQualificationsEntity educationQualificationsEntity = existingEdu.get();
                educationQualificationsEntity.setEduQualificationId(id);
                educationQualificationsEntity.setEduQualificationName(educationQualificationsDto.getEduQualificationName());
                educationQualificationsEntity.setEduDesc(educationQualificationsDto.getEduDesc());

                educationQualificationsEntity.setUpdatedBy(educationQualificationsDto.getUpdatedBy());
                educationalQualificationsRepository.save(educationQualificationsEntity);
                return educationalQualificationsMapper.toDto(educationQualificationsEntity);

            } else {
                throw new Exception("ID DOESN'T EXIST!");
            }
        } catch (Exception e) {
            return null;
        }
    }

    public EducationalQualificationsDTO deleteEduQual(Long id) {
        try {
            if (educationalQualificationsRepository.existsById(id)) {
                EducationQualificationsEntity educationQualificationsEntity =educationalQualificationsRepository.findById(id).get();
                educationQualificationsEntity.setIsActive(false);
                educationalQualificationsRepository.save(educationQualificationsEntity);
//                educationalQualificationsRepository.deleteById(id);
                return educationalQualificationsMapper.toDto(educationQualificationsEntity);
            } else {
                throw new Exception("ID DOESN'T EXIST");
            }
        } catch (Exception e) {
            return null;
        }
    }
}
