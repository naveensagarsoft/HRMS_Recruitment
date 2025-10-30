package com.bob.masterdata.Controller;

import com.bob.db.dto.ApiResponse;
import com.bob.db.dto.EducationalQualificationsDTO;
import com.bob.masterdata.Service.EducationalQualificationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/eduQual")
public class EducationalQualificationsController {

    @Autowired
    private EducationalQualificationsService educationalQualificationsService;
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<EducationalQualificationsDTO>>> getAllEduQual() throws Exception {
            List<EducationalQualificationsDTO> cities = educationalQualificationsService.getAllEduQual();
            ApiResponse<List<EducationalQualificationsDTO>> response = new ApiResponse<>(true, "DATA FIELDS FETCHED SUCCESSFULLY!", cities);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<EducationalQualificationsDTO>> createEduQual(@RequestBody EducationalQualificationsDTO educationQualificationsDto){

            EducationalQualificationsDTO educationQualificationsEntity1 = educationalQualificationsService.createEduQual(educationQualificationsDto);
            ApiResponse<EducationalQualificationsDTO> response = new ApiResponse<>(true, "DATA FIELDS CREATED SUCCESSFULLY!", educationQualificationsEntity1);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<EducationalQualificationsDTO>> updateEduQual(@PathVariable Long id, @RequestBody EducationalQualificationsDTO educationQualificationsEntity){
            EducationalQualificationsDTO msg = educationalQualificationsService.updateEduQual(id, educationQualificationsEntity);
            ApiResponse<EducationalQualificationsDTO> response = new ApiResponse<>(true, "DATA FIELD UPDATED SUCCESSFULLY!", msg);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<EducationalQualificationsDTO>> deleteEduQual(@PathVariable Long id){
            EducationalQualificationsDTO educationQualificationsDto = educationalQualificationsService.deleteEduQual(id);
            ApiResponse<EducationalQualificationsDTO> response = new ApiResponse<>(true, "DATA FIELD DELETED SUCCESSFULLY!", educationQualificationsDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
