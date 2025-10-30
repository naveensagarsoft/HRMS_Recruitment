package com.bob.masterdata.Service;

import com.bob.db.dto.SkillDTO;
import com.bob.db.entity.SkillEntity;
import com.bob.db.mapper.SkillMapper;
import com.bob.db.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SkillService {
    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private SkillMapper skillMapper;

    public SkillDTO createSkill(SkillDTO skillDto) {
        try {
            SkillEntity skill=skillMapper.toEntity(skillDto);
            skillRepository.save(skill);
            return skillMapper.toDto(skill);
        } catch (Exception e) {
            return null;
        }
    }

    public List<SkillDTO> getAllSkills() throws Exception {
        try {
            return skillMapper.toDtoList(skillRepository.findAllByIsActiveTrue());
        } catch (Exception e) {
            throw new Exception("Failed to fetch Skills");
        }
    }

    public SkillDTO updateSKill(Long id, SkillDTO skillDto) {
        try {
            SkillDTO skillDto1 = skillDto;
            Optional<SkillEntity> existingSkill = skillRepository.findById(id);
            if (existingSkill.isPresent()) {
                SkillEntity skillEntity = existingSkill.get();
                skillEntity.setSkillId(id);
                skillEntity.setSkillName(skillDto.getSkillName());
                skillEntity.setSkillDesc(skillDto.getSkillDesc());
                skillEntity.setStartDate(skillDto.getStartDate());
                skillEntity.setEndDate(skillDto.getEndDate());
                skillRepository.save(skillEntity);
                return skillMapper.toDto(skillEntity);
            } else {
                throw new Exception("ID DOESN'T EXIST!");
            }
        } catch (Exception e) {
            return null;
        }
    }

    public SkillDTO deleteSkill(Long id) {
        try {
            if (skillRepository.existsById(id)) {
                SkillEntity skillEntity =skillRepository.findById(id).get();
                skillEntity.setIsActive(false);
                skillRepository.save(skillEntity);
//                skillRepository.deleteById(id);
                return skillMapper.toDto(skillEntity);
            } else {
                throw new Exception("ID DOESN'T EXIST");
            }
        } catch (Exception e) {
            return null;
        }
    }
}
