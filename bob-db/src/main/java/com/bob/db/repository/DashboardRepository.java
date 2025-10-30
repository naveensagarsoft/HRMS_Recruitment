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

    public JsonNode getMetricsJson() throws Exception {
        String json = (String) em.createNativeQuery("SELECT dashboard_metrics()").getSingleResult();
        return objectMapper.readTree(json);
    }

    public JsonNode getQueriesJson() throws Exception {
        String json = (String) em.createNativeQuery("SELECT dashboard_queries_json()").getSingleResult();
        return objectMapper.readTree(json);
    }
}
