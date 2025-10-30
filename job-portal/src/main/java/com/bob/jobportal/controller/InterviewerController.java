package com.bob.jobportal.controller;

import com.bob.db.dto.ApiResponse;
import com.bob.db.dto.InterviewersDTO;
import com.bob.jobportal.service.InterviewerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/interviewer")
public class InterviewerController {

    @Autowired
    private InterviewerService interviewerService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<InterviewersDTO>>> getAllInterviewers(){
        List<InterviewersDTO> interviewersDTOS=interviewerService.getAllInterviewers();
        ApiResponse<List<InterviewersDTO>> listApiResponse=new ApiResponse<>(true,"Interviewers fetched successfuly",interviewersDTOS);
        return new ResponseEntity<>(listApiResponse, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<InterviewersDTO>> createInterviewer(@RequestBody InterviewersDTO interviewersDTO){
        InterviewersDTO interviewersDTO1=interviewerService.createInterviewer(interviewersDTO);
        ApiResponse<InterviewersDTO> apiResponse=new ApiResponse<>(true,"Interviewer created successfully",interviewersDTO1);
        return new ResponseEntity<>(apiResponse,HttpStatus.CREATED);
    }

    @PutMapping("/update/{interviewerId}")
    public ResponseEntity<ApiResponse<InterviewersDTO>> updateInterviewer(@PathVariable Long interviewerId, @RequestBody InterviewersDTO interviewersDTO){
        InterviewersDTO interviewersDTO1=interviewerService.updateInterviewer(interviewerId,interviewersDTO);
        ApiResponse<InterviewersDTO> interviewersDTOApiResponse=new ApiResponse<>(true,"Interviewer updated successfully",interviewersDTO1);
        return new ResponseEntity<>(interviewersDTOApiResponse,HttpStatus.OK);
    }

    @DeleteMapping("/delete/{interviewerId}")
    public ResponseEntity<ApiResponse<String>> deleteInterviewer(@PathVariable Long interviewerId){
        interviewerService.deleteInterviewer(interviewerId);
        ApiResponse<String> apiResponse=new ApiResponse<>(true,"Interviewer deleted successfully",null);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }



}
