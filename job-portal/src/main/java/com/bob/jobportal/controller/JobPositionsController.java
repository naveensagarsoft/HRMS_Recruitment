package com.bob.jobportal.controller;

import com.bob.db.dto.ApiResponse;
import com.bob.jobportal.model.JobPositionsModel;
import com.bob.jobportal.service.JobPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/job-positions")
public class JobPositionsController {

    @Autowired
    private JobPositionService jobPositionsService;


    @PostMapping("/create")
    public ResponseEntity<?> createPositions(@RequestBody JobPositionsModel positions){
        //System.out.println(positions.getSelectionProcedure());
        JobPositionsModel createdPosition = jobPositionsService.createPosition(positions);
        ApiResponse<JobPositionsModel> apiResponse = new ApiResponse<>(true, "Position created successfully", createdPosition);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updatePosition(@RequestBody JobPositionsModel positions) {
        JobPositionsModel updatedPosition = jobPositionsService.updateJobposition(positions);
        ApiResponse<JobPositionsModel> apiResponse = new ApiResponse<>(true, "Position updated successfully", updatedPosition);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/all")
    public List<JobPositionsModel> getAllPosition(){
        return jobPositionsService.findAllPositions();
    }

    @PostMapping("/create-bulk")
    public ResponseEntity<?> createBulkPositions(@RequestBody List<JobPositionsModel> positionsList) {
            for(JobPositionsModel position : positionsList) {
                if (position.getRequisitionId() == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Requisition ID is required for all positions");
                }
            }
            List<JobPositionsModel> createdPositions = jobPositionsService.createBulkPostions(positionsList);
            ApiResponse<List<JobPositionsModel>> apiResponse = new ApiResponse<>(true, "Positions created successfully", createdPositions);
            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);

    }

    @GetMapping("/get-by-requisition/{requisitionId}")
    public ResponseEntity<?> getByRequisitionId(@PathVariable("requisitionId") UUID requisitionId) {
        List<JobPositionsModel> jobPositionsDTOList = jobPositionsService.findByReqId(requisitionId);
        if (jobPositionsDTOList != null && !jobPositionsDTOList.isEmpty()) {
            ApiResponse<List<JobPositionsModel>> apiResponse = new ApiResponse<>(true, "Positions found for the given requisition ID", jobPositionsDTOList);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } else {
            ApiResponse<String> apiResponse = new ApiResponse<>(false, "No positions found for the given requisition ID", null);
            return new ResponseEntity<>(apiResponse,HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get/{positionId}")
    public ResponseEntity<?> getById(@PathVariable UUID positionId) {
        JobPositionsModel position = jobPositionsService.findByPositionId(positionId);
        if (position != null) {
            ApiResponse<JobPositionsModel> apiResponse = new ApiResponse<>(true, "Position found", position);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } else {
            ApiResponse<String> apiResponse = new ApiResponse<>(false, "Position not found", null);
            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{positionId}")
    public ResponseEntity<?> deletePosition(@PathVariable UUID positionId) {
        String result = jobPositionsService.deleteByPositionId(positionId);
        ApiResponse<String> apiResponse = new ApiResponse<>(true, "Position deleted successfully", result);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/get-active")
    public ResponseEntity<?> getActiveJobs() {
        List<JobPositionsModel> activeJobs = jobPositionsService.getActiveJobs();

        if (activeJobs.isEmpty()) {
            ApiResponse<List<JobPositionsModel>> apiResponse = new ApiResponse<>(
                    false,
                    "No active jobs found",
                    activeJobs
            );
            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        }

        ApiResponse<List<JobPositionsModel>> apiResponse = new ApiResponse<>(
                true,
                "Active jobs found",
                activeJobs
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

}