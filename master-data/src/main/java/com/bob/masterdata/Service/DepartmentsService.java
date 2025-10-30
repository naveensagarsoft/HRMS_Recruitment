package com.bob.masterdata.Service;

import com.bob.db.dto.DepartmentsDTO;
import com.bob.db.entity.DepartmentsEntity;
import com.bob.db.mapper.DepartmentsMapper;
import com.bob.db.repository.DepartmentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentsService {
    @Autowired
    private DepartmentsRepository departmentsRepository;

    @Autowired
    private DepartmentsMapper departmentsMapper;

    public DepartmentsDTO createDepartment(DepartmentsDTO departments) {
        try {
            DepartmentsEntity departmentsEntity = departmentsMapper.toEntity(departments);
            departmentsRepository.save(departmentsEntity);
            return departmentsMapper.toDto(departmentsEntity);
        } catch (Exception e) {
            return null;
        }
    }

    public List<DepartmentsDTO> getAllDepartments() throws Exception {
        try {
            return departmentsMapper.toDtoList(departmentsRepository.findAllByIsActiveTrue());
        } catch (Exception e) {
            throw new Exception("Failed to fetch departments");
        }
    }

    public DepartmentsDTO updateDepartments(Long id, DepartmentsDTO departmentsDto) {
        try {
            Optional<DepartmentsEntity> existingDepartment = departmentsRepository.findById(id);
            if (existingDepartment.isPresent()) {
                DepartmentsEntity departments=existingDepartment.get();
                departments.setDepartmentId(id);
                departments.setDepartmentName(departmentsDto.getDepartmentName());
                departments.setDepartmentDesc(departmentsDto.getDepartmentDesc());
//                departments.setActive(departmentsDto.isActive());
                departments.setUpdatedBy(departmentsDto.getUpdatedBy());
                departmentsRepository.save(departments);
                return departmentsMapper.toDto(departments);
            } else {
                throw new Exception("ID DOESN'T EXIST!");
            }
        } catch (Exception e) {
            return null;
        }
    }

    public DepartmentsDTO deleteDepartments(Long id) {
        try {
            if (departmentsRepository.existsById(id)) {
                DepartmentsEntity departments=departmentsRepository.findById(id).get();
                departments.setActive(false);
                departmentsRepository.save(departments);
//                departmentsRepository.deleteById(id);
                return departmentsMapper.toDto(departments);
            } else {
                throw new Exception("ID DOESN'T EXIST");
            }
        } catch (Exception e) {
            return null;
        }
    }
}
