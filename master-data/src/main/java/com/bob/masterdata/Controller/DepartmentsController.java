package com.bob.masterdata.Controller;

import com.bob.db.dto.ApiResponse;
import com.bob.db.dto.DepartmentsDTO;
import com.bob.masterdata.Service.DepartmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentsController {
    @Autowired
    private DepartmentsService departmentsService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<DepartmentsDTO>>> getAllCities() throws Exception {
            List<DepartmentsDTO> departments = departmentsService.getAllDepartments();
            ApiResponse<List<DepartmentsDTO>> response = new ApiResponse<>(true, "DATA FIELDS FETCHED SUCCESSFULLY!", departments);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<DepartmentsDTO>> createdepartments(@RequestBody DepartmentsDTO departments){

            DepartmentsDTO msg = departmentsService.createDepartment(departments);
            ApiResponse<DepartmentsDTO> response = new ApiResponse<>(true, "DATA FIELDS CREATED SUCCESSFULLY!", msg);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<DepartmentsDTO>> updatedepartments(@PathVariable Long id, @RequestBody DepartmentsDTO departments){
            DepartmentsDTO msg = departmentsService.updateDepartments(id, departments);
            ApiResponse<DepartmentsDTO> response = new ApiResponse<>(true, "DATA FIELD UPDATED SUCCESSFULLY!", msg);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<DepartmentsDTO>> deletedepartments(@PathVariable Long id){
            DepartmentsDTO departments = departmentsService.deleteDepartments(id);
            ApiResponse<DepartmentsDTO> response = new ApiResponse<>(true, "DATA FIELD DELETED SUCCESSFULLY!", departments);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
