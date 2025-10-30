package com.bob.masterdata.Service;

import com.bob.db.dto.SpecialCategoriesDTO;
import com.bob.db.entity.SpecialCategoriesEntity;
import com.bob.db.mapper.SpecialCategoriesMapper;
import com.bob.db.repository.SpecialCategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpecialCategoriesService {
    @Autowired
    private SpecialCategoriesRepository specialCategoriesRepository;

    @Autowired
    private SpecialCategoriesMapper specialCategoriesMapper;

    public SpecialCategoriesDTO createSpecialCategory(SpecialCategoriesDTO specialCategoriesDto) {
        try {
            SpecialCategoriesEntity specialCategoriesEntity = specialCategoriesMapper.toEntity(specialCategoriesDto);
            specialCategoriesRepository.save(specialCategoriesEntity);
            return specialCategoriesMapper.toDto(specialCategoriesEntity);
        } catch (Exception e) {
            return null;
        }
    }

    public List<SpecialCategoriesDTO> getAllSpecialCategories() throws Exception {
        try {
            return specialCategoriesMapper.toDtoList(specialCategoriesRepository.findAllByIsActiveTrue());
        } catch (Exception e) {
            throw new Exception("Failed to fetch categories");
        }
    }

    public SpecialCategoriesDTO updateSpecialCategory(Long id, SpecialCategoriesDTO specialCategoriesDto) {
        try {
            SpecialCategoriesDTO specialCategoriesEntity1 = specialCategoriesDto;
            Optional<SpecialCategoriesEntity> existingSpecialCategory = specialCategoriesRepository.findById(id);
            if (existingSpecialCategory.isPresent()) {
                SpecialCategoriesEntity specialCategoriesEntity = existingSpecialCategory.get();
                specialCategoriesEntity.setSpecialCategoryId(id);
                specialCategoriesEntity.setSpecialCategoryCode(specialCategoriesDto.getSpecialCategoryCode());
                specialCategoriesEntity.setSpecialCategoryName(specialCategoriesDto.getSpecialCategoryName());
                specialCategoriesEntity.setSpecialCategoryDesc(specialCategoriesDto.getSpecialCategoryDesc());
                //specialCategoriesEntity.setUpdatedBy(specialCategoriesDto.getUpdatedBy());
                specialCategoriesRepository.save(specialCategoriesEntity);
                return specialCategoriesMapper.toDto(specialCategoriesEntity);
            } else {
                throw new Exception("ID DOESN'T EXIST!");
            }
        } catch (Exception e) {
            return null;
        }
    }

    public SpecialCategoriesDTO deleteCategory(Long id) {
        try {
            if (specialCategoriesRepository.existsById(id)) {
                SpecialCategoriesEntity specialCategoriesEntity =specialCategoriesRepository.findById(id).get();
                specialCategoriesEntity.setIsActive(false);
                specialCategoriesRepository.save(specialCategoriesEntity);
                SpecialCategoriesDTO specialCategoriesDto =specialCategoriesMapper.toDto(specialCategoriesEntity);
//                specialCategoriesRepository.deleteById(id);
                return specialCategoriesDto;
            } else {
                throw new Exception("ID DOESN'T EXIST");
            }
        } catch (Exception e) {
            return null;
        }
    }
}
