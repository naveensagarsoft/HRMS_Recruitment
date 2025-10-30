package com.bob.jobportal.controller;

import com.bob.jobportal.repository.DashboardRepository;
import com.bob.jobportal.service.DashboardService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/metrics")
    public JsonNode getMetrics() throws Exception {
        return dashboardService.getMetrics();
    }
    @GetMapping("/queries")
    public JsonNode getQueries() throws Exception {
        return dashboardService.getQueriesJson();
    }
//    @GetMapping("get_recruitment_dashboard")
//    public JsonNode getRecruitmentDashboard() throws Exception {
//        return dashboardService.getRecruitmentDashboardJson();
//    }
}
