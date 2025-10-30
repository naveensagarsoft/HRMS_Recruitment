package com.bob.masterdata.Controller;

import com.bob.db.dto.ApiResponse;
import com.bob.db.dto.CountryDTO;
import com.bob.masterdata.Service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/country")
public class CountryController {
    @Autowired
    private CountryService countryService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<CountryDTO>>> getAllCountries() throws Exception {
        List<CountryDTO> countries = countryService.getAllCountries();
        ApiResponse<List<CountryDTO>> response = new ApiResponse<>(true, "DATA FIELDS FETCHED SUCCESSFULLY!", countries);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CountryDTO>> createcountry(@RequestBody CountryDTO country){

        CountryDTO msg = countryService.createCountry(country);
        ApiResponse<CountryDTO> response = new ApiResponse<>(true, "DATA FIELDS CREATED SUCCESSFULLY!", msg);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<CountryDTO>> updateCountry(@PathVariable Long id, @RequestBody CountryDTO country){
        CountryDTO msg = countryService.updateCountry(id, country);
        ApiResponse<CountryDTO> response = new ApiResponse<>(true, "DATA FIELD UPDATED SUCCESSFULLY!", msg);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<CountryDTO>> deleteCountry(@PathVariable Long id){
        CountryDTO country = countryService.deleteCountry(id);
        ApiResponse<CountryDTO> response = new ApiResponse<>(true, "DATA FIELD DELETED SUCCESSFULLY!", country);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
