package com.bob.masterdata.Controller;


import com.bob.db.dto.ApiResponse;
import com.bob.db.dto.SpecialCategoriesDTO;
import com.bob.masterdata.Service.SpecialCategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/special-categories")
public class SpecialCategoriesController {

    @Autowired
    private SpecialCategoriesService specialCategoriesService;


    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<SpecialCategoriesDTO>>> getAllSpecialCategories() throws Exception {
            List<SpecialCategoriesDTO> specialCategories = specialCategoriesService.getAllSpecialCategories();
            ApiResponse<List<SpecialCategoriesDTO>> response = new ApiResponse<>(true, "DATA FIELDS FETCHED SUCCESSFULLY!", specialCategories);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<SpecialCategoriesDTO>> createSpecialCategories(@RequestBody SpecialCategoriesDTO specialCategoriesDto){

            SpecialCategoriesDTO msg = specialCategoriesService.createSpecialCategory(specialCategoriesDto);
            ApiResponse<SpecialCategoriesDTO> response = new ApiResponse<>(true, "DATA FIELDS CREATED SUCCESSFULLY!", msg);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<SpecialCategoriesDTO>> updateSpecialCategories(@PathVariable Long id, @RequestBody SpecialCategoriesDTO specialCategoriesDto){
            SpecialCategoriesDTO msg = specialCategoriesService.updateSpecialCategory(id, specialCategoriesDto);
            ApiResponse<SpecialCategoriesDTO> response = new ApiResponse<>(true, "DATA FIELD UPDATED SUCCESSFULLY!", msg);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<SpecialCategoriesDTO>> deleteSpecialCategories(@PathVariable Long id){
            SpecialCategoriesDTO specialCategoriesEntity = specialCategoriesService.deleteCategory(id);
            ApiResponse<SpecialCategoriesDTO> response = new ApiResponse<>(true, "DATA FIELD DELETED SUCCESSFULLY!", specialCategoriesEntity);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
