package com.bob.jobportal.service;

import com.bob.db.entity.BulkResumeUploadEntity;
import com.bob.db.entity.CandidatesEntity;
import com.bob.db.repository.BulkResumeUploadRepository;
import com.bob.db.repository.CandidateRepository;
import com.bob.jobportal.model.ResumeEducationModel;
import com.bob.jobportal.model.ResumeExperienceModel;
import com.bob.jobportal.model.ResumeModel;
import com.bob.jobportal.util.AppConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.concurrent.DelegatingSecurityContextRunnable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

import com.bob.commonutil.service.FileService;

@Service
public class ResumeBatchService {

    @Autowired
    private OpenAIService openAIService;

    @Autowired
    private BulkResumeUploadRepository bulkResumeUploadRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private FileService fileService;

    private final ObjectMapper objectMapper;

    public ResumeBatchService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private static final Logger logger = LoggerFactory.getLogger(ResumeBatchService.class);
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    @Async
    public void runBatchProcess(String userId) {
        ForkJoinPool forkJoinPool = null;
        try {
            logger.info("Batch resume process started for user: {}", userId);

            List<BulkResumeUploadEntity> resumesUploadedList = bulkResumeUploadRepository.
                    findAllByStatusAndCreatedBy(BulkResumeUploadEntity.Status.UPLOADED, userId);

            // For each resume uploaded, process it and create a candidate entity one by one
            resumesUploadedList.stream().forEach(resume -> {
                try {
                    if (resume.getOriginalFilepath() == null || resume.getOriginalFilepath().isEmpty()) {

                        resume.setStatus(BulkResumeUploadEntity.Status.FAILED);
                        resume.setFailedReason("File URL is missing or empty");
                        logger.warn("Resume {} failed: File URL is missing or empty.", resume.getResumeId());
                        return; // Skip further processing for this resume
                    }

                    ResumeModel resumeModel = processResumeFileLLM(resume.getOriginalFilepath());

                    if (resumeModel == null) {
                        resume.setStatus(BulkResumeUploadEntity.Status.FAILED);
                        resume.setFailedReason("Failed to parse resume content or LLM returned null.");
                        logger.error("Resume {} failed: Failed to parse resume content or LLM returned null for file: {}", resume.getResumeId(), resume.getFileUrl());
                        return; // Skip further processing for this resume
                    }

                    String email = resumeModel.getEmail();
                    if (email != null && email.matches(EMAIL_REGEX)) {
                        CandidatesEntity candidate = CandidatesEntity.builder()
                                .fullName(resumeModel.getName())
                                .email(email)
                                .username(email)
                                .passwordHash("default_password")
                                .phone(resumeModel.getMobile_intl_std())
                                .fileUrl(resume.getFileUrl())
                                .address(resumeModel.getAddress())
                                .dateOfBirth(resumeModel.getDateOfBirth())
                                .gender(resumeModel.getGender())
                                .isBulkUpload(true)
                                .isActive(true)
                                .build();
                        setCurrentDesignationFromLatestExperience(candidate, resumeModel.getExperiences());
                        setLatestEducationQualification(candidate, resumeModel.getEducations());
                        setTotalExperience(candidate, resumeModel.getExperiences());
                        setCandidateOtherDetails(candidate, resumeModel);

                        try {
                            candidateRepository.save(candidate);
                            resume.setStatus(BulkResumeUploadEntity.Status.COMPLETED);
                            resume.setFailedReason(null); // Clear any previous failure reason
                            logger.info("Successfully processed and saved candidate for resume: {}", resume.getResumeId());
                        } catch (DataIntegrityViolationException ex) {
                            resume.setStatus(BulkResumeUploadEntity.Status.FAILED);
                            resume.setFailedReason("Duplicate email found for candidate.");
                            logger.warn("Resume {} failed: Duplicate email found for candidate {}: {}", resume.getResumeId(), email, ex.getMessage());
                        } catch (Exception ex) {
                            resume.setStatus(BulkResumeUploadEntity.Status.FAILED);
                            resume.setFailedReason("Candidate save failed: " + ex.getMessage());
                            logger.error("Failed to save candidate for resume {}: {}", resume.getResumeId(), ex.getMessage(), ex);
                        }
                    } else {
                        resume.setStatus(BulkResumeUploadEntity.Status.FAILED);
                        resume.setFailedReason("Invalid or missing email in parsed resume.");
                        logger.warn("Resume {} failed: Invalid or missing email in parsed resume for file: {}", resume.getResumeId(), resume.getFileUrl());
                    }
                } catch (Exception e) {
                    // Catch any unexpected exceptions during the processing of a single resume
                    resume.setStatus(BulkResumeUploadEntity.Status.FAILED);
                    resume.setFailedReason("Unexpected error during processing: " + e.getMessage());
                    logger.error("Unexpected error processing resume {}: {}", resume.getResumeId(), e.getMessage(), e);
                } finally {
                    // Always save the BulkResumeUploadEntity status, even if processing failed
                    try {
                        bulkResumeUploadRepository.save(resume);
                    } catch (Exception ex) {
                        logger.error("Failed to save BulkResumeUploadEntity status for resume {}: {}", resume.getResumeId(), ex.getMessage(), ex);
                    }
                }

            });

            logger.info("Batch resume process completed for user: {}", userId);
        } catch (Exception e) {
            logger.error("Batch resume process interrupted for user: {}", userId, e);
        } finally {
            if (forkJoinPool != null) {
                forkJoinPool.shutdown(); // Shut down the custom ForkJoinPool
            }
        }
    }

    public ResumeModel processResumeFileLLM(String filePath) {
        String fileContent = fileService.extractTextFromFile(filePath);
        if (fileContent == null || fileContent.isEmpty()) {
            logger.warn("Could not extract content from PDF for file: {}", filePath);
            return null; // FileService already logs errors, so just return null here.
        }

        try { // This try-catch now covers the rest of the LLM processing and JSON parsing
            List<String> promptList = new ArrayList<>();
            promptList.add(AppConstants.RESUME_FILE_CONTENT_LABEL);
            promptList.add(fileContent);
            promptList.add("\n\n");
            promptList.add(AppConstants.AI_PROMPT_PARSE_RESUME);
            String resumeFormatJson = loadResumeFormatJson();
            if (resumeFormatJson == null) {
                logger.error("Failed to load resume_format.json for LLM processing.");
                return null;
            }
            promptList.add(AppConstants.JSON_SCHEMA_LABEL + resumeFormatJson);
            promptList.addAll(AppConstants.AI_PROMPT_PARSE_RESUME_RULES);

            String openAiResult = openAIService.callOpenAIWithPromptList(promptList);
            // ObjectMapper objectMapper = new ObjectMapper(); // Removed this line

            // Extract the content field from the OpenAI result JSON
            String contentString = null;
            try {
                com.fasterxml.jackson.databind.JsonNode root = objectMapper.readTree(openAiResult);
                JsonNode choices = root.path("choices");
                if (choices.isArray() && choices.size() > 0) {
                    JsonNode message = choices.get(0).path("message");
                    contentString = message.path("content").asText();
                }
            } catch (Exception ex) {
                logger.error("Error extracting content from OpenAI result for file {}: {}", filePath, ex.getMessage());
                return null;
            }
            if (contentString == null || contentString.isEmpty()) {
                logger.warn("OpenAI result content is null or empty for file: {}", filePath);
                return null;
            }
            // Parse the content string into ResumeModel
            try {
                ResumeModel resumeModel = objectMapper.readValue(contentString, ResumeModel.class);
                return resumeModel;
            } catch (Exception ex) {
                logger.error("Error parsing OpenAI content string to ResumeModel for file {}: {}", filePath, ex.getMessage());
                return null;
            }
        } catch (Exception e) {
            logger.error("Unexpected error during LLM processing for file {}: {}", filePath, e.getMessage());
            return null;
        }
    }

    /**
     * Loads the resume_format.json resource from the classpath using two robust methods.
     * First tries ClassPathResource, then falls back to ClassLoader.getResourceAsStream.
     * Returns the file content as a String, or null if not found or error occurs.
     */
    private String loadResumeFormatJson() {
        try {
            // First attempt: ClassPathResource
            return new String(new ClassPathResource("resume_format.json").getInputStream().readAllBytes());
        } catch (Exception primaryEx) {
            logger.warn("ClassPathResource failed to load resume_format.json, trying ClassLoader method: {}", primaryEx.getMessage());
            try {
                InputStream is = getClass().getClassLoader().getResourceAsStream("resume_format.json");
                if (is != null) {
                    return new String(is.readAllBytes());
                } else {
                    logger.error("ClassLoader failed to load resume_format.json: resource not found");
                    return null;
                }
            } catch (Exception secondaryEx) {
                logger.error("Both resource loading methods failed for resume_format.json: {}", secondaryEx.getMessage());
                return null;
            }
        }
    }

    private void setCurrentDesignationFromLatestExperience(CandidatesEntity candidate, List<ResumeExperienceModel> experiences) {
        if (experiences != null && !experiences.isEmpty()) {
            ResumeExperienceModel latestExp = experiences.stream()
                    .max((e1, e2) -> {
                        try {
                            LocalDate d1 = e1.getEndDate() != null ? e1.getEndDate() : e1.getStartDate();
                            LocalDate d2 = e2.getEndDate() != null ? e2.getEndDate() : e2.getStartDate();
                            return d1.compareTo(d2);
                        } catch (Exception ex) {
                            return 0;
                        }
                    })
                    .orElse(null);
            if (latestExp != null) {
                candidate.setCurrentDesignation(latestExp.getJobTitle());
            }
        }
    }

    private void setLatestEducationQualification(CandidatesEntity candidate, List<ResumeEducationModel> educations) {
        if (educations != null && !educations.isEmpty()) {
            ResumeEducationModel latestEdu = educations.stream()
                    .max((e1, e2) -> {
                        try {
                            int year1 = Integer.parseInt(e1.getEndYear());
                            int year2 = Integer.parseInt(e2.getEndYear());
                            return Integer.compare(year1, year2);
                        } catch (Exception ex) {
                            return 0;
                        }
                    })
                    .orElse(null);
            if (latestEdu != null) {
                candidate.setEducationQualification(latestEdu.getDegree());
            }
        }
    }

    private void setTotalExperience(CandidatesEntity candidate, List<ResumeExperienceModel> experiences) {
        if (experiences != null && !experiences.isEmpty()) {
            try {
                LocalDate minStart = null;
                LocalDate maxEnd = null;
                for (ResumeExperienceModel exp : experiences) {
                    if (exp.getStartDate() != null) {
                        if (minStart == null || exp.getStartDate().isBefore(minStart)) {
                            minStart = exp.getStartDate();
                        }
                    }
                    if (exp.getEndDate() != null) {
                        if (maxEnd == null || exp.getEndDate().isAfter(maxEnd)) {
                            maxEnd = exp.getEndDate();
                        }
                    }
                }
                if (minStart != null && maxEnd != null && !maxEnd.isBefore(minStart)) {
                    long days = java.time.temporal.ChronoUnit.DAYS.between(minStart, maxEnd);
                    long years = days / 365;
                    long months = (days % 365) / 30;
                    String totalExp = years + " years" + (months > 0 ? " " + months + " months" : "");
                    candidate.setTotalExperience(totalExp);
                }
            } catch (Exception ex) {
                candidate.setTotalExperience(null);
                logger.error("Failed to set totalExperience for candidate {}: {}", candidate.getEmail(), ex.getMessage());
            }
        }
    }

    /**
     * Sets the candidateOtherDetails field with the stringified JSON of ResumeModel.
     * If serialization fails, sets the field to null and logs the error.
     */
    private void setCandidateOtherDetails(CandidatesEntity candidate, ResumeModel resumeModel) {
        try {
            // ObjectMapper objectMapper = new ObjectMapper(); // Removed this line
            JsonNode resumeModelJson = objectMapper.valueToTree(resumeModel);
            candidate.setCandidateOtherDetails(resumeModelJson);
        } catch (Exception ex) {
            candidate.setCandidateOtherDetails(null);
            logger.error("Failed to set candidateOtherDetails for candidate {}: {}", candidate.getEmail(), ex.getMessage());
        }
    }

}
