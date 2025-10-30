package com.bob.jobportal.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

@Service
public class OpenAIService {
    @Value("${openai.api.url}")
    private String openAIUrl;
    @Value("${openai.api.key}")
    private String apiKey;
    @Value("${openai.api.model:gpt-3.5-turbo}")
    private String openAIModel;

    static class OpenAIMessage {
        public String role;
        public String content;
        public OpenAIMessage(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }

    static class OpenAIRequest {
        public String model;
        public java.util.List<OpenAIMessage> messages;
        public OpenAIRequest(String model, java.util.List<OpenAIMessage> messages) {
            this.model = model;
            this.messages = messages;
        }
    }

    /**
     * Calls OpenAI LLM with a string prompt and returns the JSON response.
     * @param prompt The prompt to send to OpenAI.
     * @return JSON response from OpenAI.
     */
    public String callOpenAIWithPrompt(String prompt) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            OpenAIMessage message = new OpenAIMessage("user", prompt);
            OpenAIRequest request = new OpenAIRequest(openAIModel, Collections.singletonList(message));
            String requestBody = objectMapper.writeValueAsString(request);

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(openAIUrl, HttpMethod.POST, entity, String.class);
            return response.getBody(); // JSON response from OpenAI
        } catch (Exception e) {
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }

    /**
     * Calls OpenAI LLM with a list of prompt strings, returns the JSON response.
     * Each prompt is sent as a separate message in the request.
     * @param prompts List of prompt strings to send to OpenAI.
     * @return JSON response from OpenAI.
     */
    public String callOpenAIWithPromptList(java.util.List<String> prompts) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<OpenAIMessage> messages = new ArrayList<>();
            for (String prompt : prompts) {
                messages.add(new OpenAIMessage("user", prompt));
            }
            OpenAIRequest request = new OpenAIRequest(openAIModel, messages);
            String requestBody = objectMapper.writeValueAsString(request);

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(openAIUrl, HttpMethod.POST, entity, String.class);
            return response.getBody(); // JSON response from OpenAI
        } catch (Exception e) {
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }

}
