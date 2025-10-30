package com.bob.candidateportal.Controllers;

import com.bob.candidateportal.model.*;
import com.bob.db.dto.InterviewsDTO;
import com.bob.candidateportal.Service.CalendarService;
import com.bob.candidateportal.Service.CandidateService;
import com.bob.candidateportal.Service.MailService;
import com.bob.db.dto.ApiResponse;
import com.bob.db.dto.*;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/candidates")
public class CandidatesController {
    @Autowired
    private CandidateService candidateService;


    @Autowired
    private MailService mailService;

    @Autowired
    private CalendarService calendarService;

    @GetMapping("/test/exception")
    public void testExc() throws  Exception{
        throw new Exception("naveen test");
    }

    @GetMapping("/get-by-position/{positionId}")
    public ResponseEntity<ApiResponse<List<CandidatesDTO>>> getDetailsByPositionId(@PathVariable UUID positionId) throws Exception {
        List<CandidatesDTO> candidateDetailsList = candidateService.getDetailsByPositionId(positionId);
        if (candidateDetailsList==null || candidateDetailsList.isEmpty() ) {
            ApiResponse<List<CandidatesDTO>> response = new ApiResponse<>(false, "No candidates found for the given position ID", Collections.emptyList());
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        }
        ApiResponse<List<CandidatesDTO>> response = new ApiResponse<>(true, "Candidate details retrieved successfully", candidateDetailsList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/details-by-position/{positionId}")
    public ResponseEntity<ApiResponse<?>> getCandidateDetailsByPositionId(@PathVariable UUID positionId) {
        List<GetCandidateDetailsByPositionIdResponse> candidateDetailsList =
                candidateService.getCandidateDetailsByPositionId(positionId);

        if (candidateDetailsList == null || candidateDetailsList.isEmpty()) {
            ApiResponse<String> response = new ApiResponse<>(
                    false,
                    "No candidates found for position",
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        ApiResponse<List<GetCandidateDetailsByPositionIdResponse>> response = new ApiResponse<>(
                true,
                "Candidates fetched successfully",
                candidateDetailsList
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-by-candidate/{candidateId}")
    public ResponseEntity<ApiResponse<CandidatesDTO>> getCandidateDetailsById(@PathVariable UUID candidateId) {
        CandidatesDTO candidate = candidateService.getCandidatesById(candidateId);

        if (candidate == null) {
            ApiResponse<CandidatesDTO> response = new ApiResponse<>(
                    false,
                    "No candidate found for the given ID: " + candidateId,
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        ApiResponse<CandidatesDTO> response = new ApiResponse<>(
                true,
                "Candidate details retrieved successfully",
                candidate
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/schedule-interview")
    public ResponseEntity<String> scheduleInterview(@RequestBody ScheduleInterviewRequest scheduleInterviewRequest) throws Exception {
        return new ResponseEntity<>(candidateService.scheduleInterview(scheduleInterviewRequest),HttpStatus.OK);
    }

    @PutMapping("/offer")
    public String offer(@RequestBody OfferDTO offerdto) throws MessagingException, UnsupportedEncodingException {
        return candidateService.offer(offerdto);
    }

    @GetMapping("/get-by-status/{status}")
    public List<CandidateDetailsResponse> getCandidateByStatus(@PathVariable String status) {
        return candidateService.getCandidateByApplicationStatus(status);
    }

    @GetMapping("/get-count-by-status/{status}")
    public Integer getCountByStatus(@PathVariable String status) {
        return candidateService.countCandidatesByStatus(status);
    }


    @GetMapping("/interviews/{application_id}")
    public  ResponseEntity<?> getInterviewDetailsByCandidateId(@PathVariable UUID application_id) {

            InterviewerResponse interviewerResponse = candidateService
                    .getInterviewsByApplicationId(application_id);
            if (interviewerResponse == null) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body("No interviews found for the given candidate ID.");
            }

            return ResponseEntity.ok(interviewerResponse);
    }



    @PostMapping("/apply/job")
    public ResponseEntity<String> shortlist(@RequestBody ShortlistCandidateDTO shortlistCandidateDTO) throws Exception {
        String str = candidateService.shortlistCandidate(shortlistCandidateDTO);
        if(!"Applied for Job!".equals(str)) {
            throw new Exception(str);
        }
        return new ResponseEntity<>(str,HttpStatus.OK);
    }

    //Adding data to candidate table
    @PutMapping("/update_candidate")
    public ResponseEntity<ApiResponse<?>> createCandidate(@RequestBody CandidatesDTO candidate) throws Exception {
        CandidatesDTO createdCandidate = candidateService.updateCandidate(candidate);
        ApiResponse<?> response = new ApiResponse<>(true, "Candidate created successfully", createdCandidate);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }



    @GetMapping("get-applied-positions/{candidate_id}")
    public ResponseEntity<List<JobPositionsModel>> getDetailsByCandidateId(@PathVariable UUID candidate_id) throws Exception {
        List<JobPositionsModel> positionDTOList = candidateService.getAllDetailsByCandidateId(candidate_id);
        return new ResponseEntity<>(positionDTOList,HttpStatus.OK);
    }

    @GetMapping("/interviews/by-date-range")
    public ResponseEntity<List<InterviewResponse>> getInterviewsByDateRange(
            @RequestParam long startTimestamp,
            @RequestParam long endTimestamp) throws Exception {
        List<InterviewResponse> interviewsResponseList = calendarService.getInterviewSchedulesBetween(startTimestamp, endTimestamp);
        if (interviewsResponseList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(interviewsResponseList, HttpStatus.OK);
    }



    // new apis
    @PostMapping("/feedback")
    public ResponseEntity<String> submitFeedback(@RequestBody FeedbackRequest feedbackRequest) throws Exception {
        String result = candidateService.submitFeedback(feedbackRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/get-feedback/{applicationId}")
    public ResponseEntity<List<?>> getAllFeedback(@PathVariable UUID applicationId) throws Exception {
        List<FeedbackResponse> feedbackList = candidateService.getFeedbackByApplicationId(applicationId);
        if (feedbackList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(feedbackList, HttpStatus.OK);
    }


    @GetMapping("/panel-free-slots")
    public ResponseEntity<ApiResponse<?>> getPanelFreeSlots(@RequestParam Long panelId, @RequestParam String date) throws Exception {
        List<Map<String,Object>> freeSlots = candidateService.getPanelSlots( panelId,date);
        ApiResponse<List<Map<String, Object>>> response = new ApiResponse<>(true,"Free slots",freeSlots);
        return ResponseEntity.ok(response);
     }
  
    @GetMapping("/not-applied-bulk-upload/{positionId}")
    public ResponseEntity<ApiResponse<List<CandidatesDTO>>> getCandidatesNotAppliedBulkUpload(@PathVariable UUID positionId) {
        List<CandidatesDTO> candidates = candidateService.getCandidatesNotAppliedBulkUpload(positionId);
        if (candidates == null || candidates.isEmpty()) {
            ApiResponse<List<CandidatesDTO>> response = new ApiResponse<>(false, "No bulk-uploaded candidates found who have not applied for this position", Collections.emptyList());
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        }
        ApiResponse<List<CandidatesDTO>> response = new ApiResponse<>(true, "Bulk-uploaded candidates fetched successfully", candidates);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/bulk-shortlist")
    public ResponseEntity<ApiResponse<Map<UUID, String>>> bulkShortlist(@RequestBody BulkShortlistRequest request) {
        Map<UUID, String> result = candidateService.bulkShortlistCandidates(request);
        boolean allSuccess = result.values().stream().allMatch(msg -> "Applied for Job!".equals(msg));
        ApiResponse<Map<UUID, String>> response = new ApiResponse<>(allSuccess, allSuccess ? "All candidates shortlisted successfully" : "Some candidates could not be shortlisted", result);
        return new ResponseEntity<>(response, allSuccess ? HttpStatus.OK : HttpStatus.MULTI_STATUS);
    }

}