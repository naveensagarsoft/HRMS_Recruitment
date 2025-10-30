package com.bob.masterdata.Controller;

import com.bob.db.dto.ApiResponse;
import com.bob.db.dto.JobGradeDTO;
import com.bob.masterdata.Model.InterviewPanelsModel;
import com.bob.masterdata.Service.InterviewPanelsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/interview-panels")
public class InterviewPanelsController {

    @Autowired
    private InterviewPanelsService interviewPanelsService;


    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<InterviewPanelsModel>>> getAllInterviewPanels() {
        List<InterviewPanelsModel> interviewPanels = interviewPanelsService.getAllInterviewPanels();
        ApiResponse<List<InterviewPanelsModel>> response = new ApiResponse<>(true,"get All the panels",interviewPanels);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<InterviewPanelsModel>>  CreateInterviewPanel(@RequestBody InterviewPanelsModel interviewPanelsModel) {
        InterviewPanelsModel interviewPanelsModel1 = interviewPanelsService.createInterviewPanel(interviewPanelsModel);
        ApiResponse<InterviewPanelsModel> response = new ApiResponse<>(true, "Panel Created Successfully!", interviewPanelsModel1);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update/{panel_id}")
    public ResponseEntity<ApiResponse<InterviewPanelsModel>> updateInterviewPanel(@PathVariable Long panel_id, @RequestBody InterviewPanelsModel interviewPanelsModel) {
        InterviewPanelsModel interviewPanelsModel1 = interviewPanelsService.updateInterviewPanel(panel_id,interviewPanelsModel);
        ApiResponse<InterviewPanelsModel> response = new ApiResponse<>(true, "Panel Updated Successfully!", interviewPanelsModel1);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{panel_id}")
    public ResponseEntity<ApiResponse<?>> deleteInterviewPanel(@PathVariable Long panel_id) {
        interviewPanelsService.deleteInterviewPanel(panel_id);
        ApiResponse<InterviewPanelsModel> response = new ApiResponse<>(true, "Panel Deleted Successfully!",null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/active-members")
    public  ResponseEntity<ApiResponse<List<Long>>> activePanelNumbers(){
        List<Long> interviewPanels = interviewPanelsService.activePanelNumbers();
        ApiResponse<List<Long>> response = new ApiResponse<>(true, "get All the panels",interviewPanels);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // get panel times

}
