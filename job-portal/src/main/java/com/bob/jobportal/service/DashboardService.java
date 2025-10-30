package com.bob.jobportal.service;

import com.bob.jobportal.repository.DashboardRepository;
import com.bob.jobportal.util.SecurityUtils;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DashboardService {
    @Autowired
    private DashboardRepository dashboardRepository;

    @Autowired
    private SecurityUtils securityUtils;
    public JsonNode getMetrics() throws Exception {
        if (securityUtils.isAdmin()){
            return dashboardRepository.getMetricsJson(null,securityUtils.getCurrentUserRole());
        } else {
            return dashboardRepository.getMetricsJson( securityUtils.getCurrentUserToken(),securityUtils.getCurrentUserRole());
        }

    }

    public JsonNode getQueriesJson() throws Exception {
        if (securityUtils.isAdmin()){
            return dashboardRepository.getQueriesJson(null, securityUtils.getCurrentUserRole());
        } else {
            return dashboardRepository.getQueriesJson( securityUtils.getCurrentUserToken(), securityUtils.getCurrentUserRole());
        }
    }

//    public JsonNode getRecruitmentDashboardJson() throws Exception {
//        if (securityUtils.isAdmin()){
//            return dashboardRepository.getRecruitmentDashboardJson(null);
//        } else {
//            return dashboardRepository.getRecruitmentDashboardJson( securityUtils.getCurrentUserId());
//        }
//    }
}
