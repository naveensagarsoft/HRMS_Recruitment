package com.bob.masterdata.Service;

import com.bob.db.dto.JobGradeDTO;
import com.bob.db.entity.JobGradeEntity;
import com.bob.db.mapper.JobGradeMapper;
import com.bob.db.repository.JobGradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobGradeService {

    @Autowired
    private JobGradeRepository jobGradeRepository;

    @Autowired
    private JobGradeMapper jobGradeMapper;

    public JobGradeDTO createJobGrade(JobGradeDTO jobGradeDto) {
        try {
            jobGradeRepository.save(jobGradeMapper.toEntity(jobGradeDto));
            return jobGradeDto;
        } catch (Exception e) {
            return null;
        }
    }

    public List<JobGradeDTO> getAllJobGrades() throws Exception {
        try {
            return jobGradeMapper.toDtoList(jobGradeRepository.findAllByIsActiveTrue());
        } catch (Exception e) {
            throw new Exception("Failed to fetch JobGrades");
        }
    }

    public JobGradeDTO updateJobGrade(Long id, JobGradeDTO jobGradeDto) {
        try {
//            JobGradeDTO jobGradeDto1 = jobGradeDto;
            Optional<JobGradeEntity> existingJobGrade = jobGradeRepository.findById(id);
            if (existingJobGrade.isPresent()) {
                JobGradeEntity jobGradeEntity = existingJobGrade.get();
                jobGradeEntity.setJobGradeId(id);
                jobGradeEntity.setJobGradeCode(jobGradeDto.getJobGradeCode());
                jobGradeEntity.setJobGradeDesc(jobGradeDto.getJobGradeDesc());
                jobGradeEntity.setJobScale(jobGradeDto.getJobScale());
                jobGradeEntity.setMinSalary(jobGradeDto.getMinSalary());
                jobGradeEntity.setMaxSalary(jobGradeDto.getMaxSalary());
                jobGradeEntity.setEffectiveStateDate(jobGradeDto.getEffectiveStateDate());
                jobGradeEntity.setEffectiveEndDate(jobGradeDto.getEffectiveEndDate());
                jobGradeRepository.save(jobGradeEntity);
                return jobGradeMapper.toDto(jobGradeEntity);

            } else {
                throw new Exception("ID DOESN'T EXIST!");
            }
        } catch (Exception e) {
            return null;
        }
    }

    public JobGradeDTO deleteJobGrade(Long id) {
        try {
            if (jobGradeRepository.existsById(id)) {
                JobGradeEntity jobGradeEntity =jobGradeRepository.findById(id).get();
                jobGradeEntity.setActive(false);
                jobGradeRepository.save(jobGradeEntity);
//                jobGradeRepository.deleteById(id);
                return jobGradeMapper.toDto(jobGradeEntity);
            } else {
                throw new Exception("ID DOESN'T EXIST");
            }
        } catch (Exception e) {
            return null;
        }
    }
}
