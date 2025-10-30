package com.bob.masterdata.Controller;

import com.bob.db.dto.ApiResponse;
import com.bob.db.dto.ReservationCategoriesDTO;
import com.bob.masterdata.Service.ReservationCategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class ReservationCategoriesController {
    @Autowired
    private ReservationCategoriesService reservationCategoriesService;


    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<ReservationCategoriesDTO>>> getAllReservations() throws Exception {
            List<ReservationCategoriesDTO> resCategories = reservationCategoriesService.getAllResCategories();
            ApiResponse<List<ReservationCategoriesDTO>> response = new ApiResponse<>(true, "DATA FIELDS FETCHED SUCCESSFULLY!", resCategories);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<ReservationCategoriesDTO>> createReservation(@RequestBody ReservationCategoriesDTO reservationCategoriesDto){

            ReservationCategoriesDTO reservationCategoriesEntity1 = reservationCategoriesService.createReservationCategories(reservationCategoriesDto);
            ApiResponse<ReservationCategoriesDTO> response = new ApiResponse<>(true, "DATA FIELDS CREATED SUCCESSFULLY!", reservationCategoriesEntity1);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<ReservationCategoriesDTO>> updateReservations(@PathVariable Long id, @RequestBody ReservationCategoriesDTO reservationCategoriesDto){
            ReservationCategoriesDTO reservationCategoriesEntity1 = reservationCategoriesService.updateResCategories(id, reservationCategoriesDto);
            ApiResponse<ReservationCategoriesDTO> response = new ApiResponse<>(true, "DATA FIELD UPDATED SUCCESSFULLY!", reservationCategoriesEntity1);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<ReservationCategoriesDTO>> deleteReservations(@PathVariable Long id){
            ReservationCategoriesDTO reservationCategoriesEntity = reservationCategoriesService.deleteReservationCategory(id);
            ApiResponse<ReservationCategoriesDTO> response = new ApiResponse<>(true, "DATA FIELD DELETED SUCCESSFULLY!", reservationCategoriesEntity);
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
