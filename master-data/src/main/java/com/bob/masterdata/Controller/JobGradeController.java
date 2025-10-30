package com.bob.masterdata.Controller;

import com.bob.db.dto.ApiResponse;
import com.bob.db.dto.JobGradeDTO;
import com.bob.masterdata.Service.JobGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobgrade")
public class JobGradeController {
    @Autowired
    private JobGradeService jobGradeService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<JobGradeDTO>>> getAllJobGrades() throws Exception {
            List<JobGradeDTO> jobGrades = jobGradeService.getAllJobGrades();
            ApiResponse<List<JobGradeDTO>> response = new ApiResponse<>(true, "DATA FIELDS FETCHED SUCCESSFULLY!", jobGrades);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<JobGradeDTO>> createJobGrade(@RequestBody JobGradeDTO jobGradeDto){

            JobGradeDTO jobGradeDto1 = jobGradeService.createJobGrade(jobGradeDto);
            ApiResponse<JobGradeDTO> response = new ApiResponse<>(true, "DATA FIELDS CREATED SUCCESSFULLY!", jobGradeDto1);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<JobGradeDTO>> updateJobGrade(@PathVariable Long id, @RequestBody JobGradeDTO jobGradeDto){
            JobGradeDTO msg = jobGradeService.updateJobGrade(id, jobGradeDto);
            ApiResponse<JobGradeDTO> response = new ApiResponse<>(true, "DATA FIELD UPDATED SUCCESSFULLY!", msg);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<JobGradeDTO>> deleteJobGrade(@PathVariable Long id){
            JobGradeDTO jobGradeDto = jobGradeService.deleteJobGrade(id);
            ApiResponse<JobGradeDTO> response = new ApiResponse<>(true, "DATA FIELD DELETED SUCCESSFULLY!", jobGradeDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
