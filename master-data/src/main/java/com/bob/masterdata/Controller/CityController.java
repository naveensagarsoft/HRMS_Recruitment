package com.bob.masterdata.Controller;

import com.bob.db.dto.ApiResponse;
import com.bob.db.dto.CityDTO;
import com.bob.masterdata.Service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/city")
public class CityController {
    @Autowired
    private CityService cityService;


    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<CityDTO>>> getAllCities() throws Exception {
            List<CityDTO> cities = cityService.getAllCities();
            ApiResponse<List<CityDTO>> response = new ApiResponse<>(true, "DATA FIELDS FETCHED SUCCESSFULLY!", cities);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CityDTO>> createCity(@RequestBody CityDTO city){

            CityDTO msg = cityService.createCity(city);
            ApiResponse<CityDTO> response = new ApiResponse<>(true, "DATA FIELDS CREATED SUCCESSFULLY!", msg);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<CityDTO>> updateCity(@PathVariable Long id, @RequestBody CityDTO city){
            CityDTO msg = cityService.updateCities(id, city);
            ApiResponse<CityDTO> response = new ApiResponse<>(true, "DATA FIELD UPDATED SUCCESSFULLY!", msg);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<CityDTO>> deleteCity(@PathVariable Long id){
            CityDTO city = cityService.deleteCities(id);
            ApiResponse<CityDTO> response = new ApiResponse<>(true, "DATA FIELD DELETED SUCCESSFULLY!", city);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
