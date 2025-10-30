package com.bob.masterdata.Controller;


import com.bob.db.dto.ApiResponse;
import com.bob.db.dto.LocationDTO;
import com.bob.masterdata.Service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/location")
public class LocationController {
    @Autowired
    private LocationService locationService;
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<LocationDTO>>> getAllLocations() throws Exception {
        List<LocationDTO> locations = locationService.getAllLocations();
        ApiResponse<List<LocationDTO>> response = new ApiResponse<>(true, "DATA FIELDS FETCHED SUCCESSFULLY!", locations);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<LocationDTO>> createLocation(@RequestBody LocationDTO location){

        LocationDTO location1 = locationService.createLocation(location);
        ApiResponse<LocationDTO> response = new ApiResponse<>(true, "DATA FIELDS CREATED SUCCESSFULLY!", location1);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<LocationDTO>> updateLocation(@PathVariable Long id,@RequestBody LocationDTO location){
        LocationDTO location1=locationService.updateLocations(id,location);
        ApiResponse<LocationDTO> response=new ApiResponse<>(true,"DATA FIELD UPDATED SUCCESSFULLY!",location1);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<LocationDTO>> deleteLocation(@PathVariable Long id){
        LocationDTO location1=locationService.deleteLocation(id);
        ApiResponse<LocationDTO> response=new ApiResponse<>(true,"DATA FIELD DELETED SUCCESSFULLY!",location1);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
