package com.bob.candidateportal.Feign;

import com.bob.candidateportal.model.JobPositionsModel;
import com.bob.db.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "positions-service",url = "https://bobjava.sentrifugo.com:8443")
public interface FeignPositionDTO {

    @GetMapping("/jobcreation/api/v1/job-positions/get-active")
    public ResponseEntity<ApiResponse<List<JobPositionsModel>>> getActiveJobs(@RequestHeader("Authorization") String token) ;
}
