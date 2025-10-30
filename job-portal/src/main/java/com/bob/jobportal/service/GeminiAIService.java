package com.bob.jobportal.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;

@Service
public class GeminiAIService {
    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    public String callGeminiWithPrompt(String prompt) {
        String geminiUrl = geminiApiUrl + "?key=" + geminiApiKey;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            GeminiPart part = new GeminiPart(prompt);
            GeminiContent content = new GeminiContent(Collections.singletonList(part));
            GeminiRequest request = new GeminiRequest(Collections.singletonList(content));
            String requestBody = objectMapper.writeValueAsString(request);
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(geminiUrl, HttpMethod.POST, entity, String.class);
            return response.getBody();
        } catch (Exception e) {
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }

    static class GeminiRequest {
        public java.util.List<GeminiContent> contents;
        public GeminiRequest(java.util.List<GeminiContent> contents) {
            this.contents = contents;
        }
    }
    static class GeminiContent {
        public java.util.List<GeminiPart> parts;
        public GeminiContent(java.util.List<GeminiPart> parts) {
            this.parts = parts;
        }
    }
    static class GeminiPart {
        public String text;
        public GeminiPart(String text) {
            this.text = text;
        }
    }
}
