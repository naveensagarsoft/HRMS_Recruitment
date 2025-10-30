package com.bob.jobportal.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class DashboardRepository {

    @PersistenceContext
    private EntityManager em;

    private final ObjectMapper objectMapper;

    public DashboardRepository(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public JsonNode getMetricsJson(String userId, String role ) throws Exception {
        String json = (String) em.createNativeQuery("SELECT dashboard_metrics(:userId,:role)")
                .setParameter("userId", userId)
                .setParameter("role",role)
                .getSingleResult();
        return objectMapper.readTree(json);
    }

    public JsonNode getQueriesJson(String userId,String role) throws Exception {
        String json = (String) em.createNativeQuery("SELECT dashboard_queries_json(:userId,:role)")
                .setParameter("userId", userId)
                .setParameter("role",role).getSingleResult();
        return objectMapper.readTree(json);
    }


//    public JsonNode getRecruitmentDashboardJson(String userId) throws Exception {
//        String json = (String) em.createNativeQuery("SELECT * FROM get_recruitment_dashboard_json('"+userId+"');").getSingleResult();
//        return objectMapper.readTree(json);
//    }
}
