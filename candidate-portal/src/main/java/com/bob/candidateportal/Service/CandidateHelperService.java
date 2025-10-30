package com.bob.candidateportal.Service;


import com.bob.candidateportal.model.ScheduleInterviewRequest;
import com.bob.candidateportal.util.AppConstants;
import com.bob.db.entity.*;
import com.bob.db.repository.*;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Helper service for candidate interview scheduling, rescheduling, cancelation
 * and email notifications with calendar invites.
 */
@Slf4j
@Service
public class CandidateHelperService {

    @Autowired private MailService mailService;

    @Autowired private InterviewsRepository interviewsRepository;

    @Autowired private MeetScheduler meetScheduler;

    @Autowired private PositionsRepository positionsRepository;

    @Autowired private CandidateRepository candidateRepository;

    @Autowired private CandidateDocumentsRepository candidateDocumentsRepository;

    @Autowired private InterviewersRepository interviewersRepository;

    @Autowired private InterviewPanelsRepository interviewPanelsRepository;

    @Autowired private InterviewPanelMembersRepository interviewPanelMembersRepository;

    private static final Logger logger = LoggerFactory.getLogger(CandidateHelperService.class);

    public enum CalendarAction { SCHEDULE, RESCHEDULE, CANCEL }


    public InterviewsEntity getOrCreateInterview(ScheduleInterviewRequest req) {
        InterviewsEntity interviews = interviewsRepository.findByApplicationId(req.getApplicationId()).orElse(null);

        return interviews == null
                ? InterviewsEntity.builder()
                .applicationId(req.getApplicationId())
                .type(req.getType())
                .scheduledAt(LocalDateTime.of(req.getDate(), req.getTime()))
                .status(req.getStatus())
                .interviewerId(req.getInterviewerId())
                .isPanelInterview(req.getIsPanelInterview())
                .phone(req.getPhone())
                .location(req.getLocation())
                .build()
                : interviews;
    }

    private Map<String, Object> prepareEmailData(
            InterviewsEntity interview, CandidatesEntity candidate,
            PositionsEntity position, CandidateApplicationsEntity application) {

        String name ="";
        if(interview.getIsPanelInterview()){
            name = interviewPanelsRepository.findById(interview.getInterviewerId()).get().getPanelName();
        }else{
            name = interviewersRepository.findById(interview.getInterviewerId()).get().getFullName();
        }


        Map<String, Object> emailData = new HashMap<>();
        emailData.put(AppConstants.EMAIL_DATA_TIME, interview.getScheduledAt().toLocalTime().toString());
        emailData.put(AppConstants.EMAIL_DATA_DATE, interview.getScheduledAt().toLocalDate().toString());
        emailData.put(AppConstants.EMAIL_DATA_INTERVIEWER_NAME,name);
        emailData.put(AppConstants.EMAIL_DATA_CANDIDATE_NAME, candidate.getFullName());
        emailData.put(AppConstants.EMAIL_DATA_CANDIDATE_EMAIL, candidate.getEmail());
        emailData.put(AppConstants.EMAIL_DATA_CANDIDATE_POSITION_TITLE, position.getPositionTitle());
        emailData.put(AppConstants.EMAIL_DATE_TIME, interview.getScheduledAt());
        emailData.put(AppConstants.EMAIL_DATA_CANDIDATE_APPLICATION_ID, application.getApplicationId());
        emailData.put(AppConstants.EMAIL_DATA_INTERVIEW_MODE, interview.getType());
        String subject="Interview "+interview.getStatus()+" "+ position.getPositionTitle();
        if(interview.getStatus().equalsIgnoreCase(AppConstants.CAND_APP_STATUS_SCHEDULED)){
            handleSchedule(interview,emailData,subject);
        }else if(interview.getStatus().equalsIgnoreCase(AppConstants.CAND_APP_STATUS_RESCHEDULED)){
            handleReschedule(interview,emailData,subject);
        }else if(interview.getStatus().equalsIgnoreCase(AppConstants.CAND_APP_STATUS_CANCELLED)){
            handleCancel(interview,emailData);
        }
        return emailData;
    }

    private void handleSchedule( InterviewsEntity interview, Map<String, Object> emailData, String subject) {
        LocalDateTime scheduleTime = interview.getScheduledAt();
        emailData.put(AppConstants.EMAIL_DATA_INTERVIEW_MODE,interview.getType());
        if(interview.getType().equalsIgnoreCase("Online")){
            String meetingLink = meetScheduler.createMeetingUrl(subject, scheduleTime.toString(), scheduleTime.plusHours(1).toString());
            logger.info(meetingLink);
            emailData.put(AppConstants.EMAIL_DATA_LINK, meetingLink);
        }else if(interview.getType().equalsIgnoreCase("In-Person")){
            logger.info(interview.getLocation());
            emailData.put(AppConstants.EMAIL_DATA_LOCATION, interview.getLocation());
        }else if(interview.getType().equalsIgnoreCase("Telephonic")){
            logger.info(interview.getPhone());
            emailData.put(AppConstants.EMAIL_DATA_PHONE_NUMBER, interview.getPhone());
        }

        emailData.put(AppConstants.EMAIL_DATA_STATUS, AppConstants.CAND_APP_STATUS_SCHEDULED);

        interview.setScheduledAt(scheduleTime);
        interview.setStatus(AppConstants.CAND_APP_STATUS_SCHEDULED);
    }

    private void handleReschedule( InterviewsEntity interview, Map<String, Object> emailData, String subject) {
        LocalDateTime scheduleTime = interview.getScheduledAt();
        if(interview.getType().equalsIgnoreCase("Online")){
            String meetingLink = meetScheduler.createMeetingUrl(subject, scheduleTime.toString(), scheduleTime.plusHours(1).toString());

            emailData.put(AppConstants.EMAIL_DATA_LINK, meetingLink);
        }else if(interview.getType().equalsIgnoreCase("In-Person")){
            emailData.put(AppConstants.EMAIL_DATA_LOCATION, interview.getLocation());
        }else if(interview.getType().equalsIgnoreCase("Telephonic")){
            emailData.put(AppConstants.EMAIL_DATA_PHONE_NUMBER, interview.getPhone());
        }

        emailData.put(AppConstants.EMAIL_DATA_STATUS, AppConstants.CAND_APP_STATUS_RESCHEDULED);

        interview.setScheduledAt(scheduleTime);
        interview.setStatus(AppConstants.CAND_APP_STATUS_RESCHEDULED);
    }

    private void handleCancel( InterviewsEntity interview, Map<String, Object> emailData) {
        emailData.put(AppConstants.EMAIL_DATA_STATUS, AppConstants.CAND_APP_STATUS_CANCELLED);
        emailData.put(AppConstants.PREVIOUS_INTERVIEW_TIMESTAMP, interview.getScheduledAt());

        interview.setStatus(AppConstants.CAND_APP_STATUS_CANCELLED);
    }


    private boolean sendEmailWithRetry(String toEmail, String subject, String template, Map<String, Object> variables) {
        for (int i = 0; i < 3; i++) {
            if ("Mail Sent!".equals(mailService.sendSimpleEmail(toEmail, subject, template, variables))) {
                return true;
            }
        }
        return false;
    }

    public boolean sendEmailWithAttachmentRetry(String toEmail, String subject, String template,
                                                Map<String, Object> variables, String path)
            throws MessagingException, UnsupportedEncodingException {

        if (path == null || path.isEmpty()) {
            return "Mail Sent!".equals(mailService.sendSimpleEmail(toEmail, subject, template, variables));
        }

        for (int i = 0; i < 3; i++) {
            if ("Mail Sent with attachment!".equals(mailService.sendEmailWithAttachment(toEmail, subject, path, variables, template))) {
                return true;
            }
        }
        return false;
    }



    /** ============================= CALENDAR INVITE ============================= */

    @Async
    public void sendEmailScheduleForCandidatesAndInterviews(InterviewsEntity interviewsEntity,CandidateApplicationsEntity candidateApplicationsEntity) {

        CandidatesEntity candidate = candidateRepository.findById(candidateApplicationsEntity.getCandidateId())
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        PositionsEntity position = positionsRepository.findById(candidateApplicationsEntity.getPositionId())
                .orElseThrow(() -> new RuntimeException("Position not found"));

        // Assuming interviewer details need to set manually or fetched from another service
        Map<String, Object> emailData = prepareEmailData(interviewsEntity, candidate, position, candidateApplicationsEntity);

        List<InterviewersEntity> interviewersEntitiesList;

        if(interviewsEntity.getIsPanelInterview()){
            Long panelId=interviewsEntity.getInterviewerId();
            InterviewPanelsEntity interviewPanels  =interviewPanelsRepository.findByPanelIdAndIsActiveTrue(panelId).orElseThrow(
                    () -> new RuntimeException("Interview Panel not found"));
            List<InterviewPanelMembersEntity> panelMembers = interviewPanelMembersRepository.findAllByPanelId(panelId);
            List<Long> interviewerIds = panelMembers.stream().map(
                    InterviewPanelMembersEntity::getInterviewerId
            ).toList();
            interviewersEntitiesList = interviewersRepository.findAllById(interviewerIds);
        }else{
            interviewersEntitiesList = List.of(interviewersRepository.findById(interviewsEntity.getInterviewerId()).orElseThrow(
                    () -> new RuntimeException("Interviewer not found")
            ));
        }


        String actionStatus = interviewsEntity.getStatus();

        CalendarAction action = switch (actionStatus) {
            case AppConstants.CAND_APP_STATUS_SCHEDULED -> CalendarAction.SCHEDULE;
            case AppConstants.CAND_APP_STATUS_RESCHEDULED -> CalendarAction.RESCHEDULE;
            case AppConstants.CAND_APP_STATUS_CANCELLED -> CalendarAction.CANCEL;
            default -> throw new IllegalArgumentException("Unsupported status: " + actionStatus);
        };

        String calendarContent = buildCalendarContent(candidateApplicationsEntity.getApplicationId().toString(), emailData, action);

        try {
            String subject = switch (action) {
                case SCHEDULE -> "Interview Scheduled: " + position.getPositionTitle();
                case RESCHEDULE -> "Interview Rescheduled: " + position.getPositionTitle();
                case CANCEL -> "Interview Cancelled: " + position.getPositionTitle();
            };
            String templateCandidate = switch (action) {
                case SCHEDULE -> AppConstants.EMAIL_TEMPLATE_CANDIDATE_INTERVIEW_SCHEDULE;
                case RESCHEDULE -> AppConstants.EMAIL_TEMPLATE_CANDIDATE_INTERVIEW_RESCHEDULE;
                case CANCEL -> AppConstants.EMAIL_TEMPLATE_CANDIDATE_INTERVIEW_CANCEL;
            };
            String templateInterviewer = switch (action) {
                case SCHEDULE -> AppConstants.EMAIL_TEMPLATE_INTERVIEWER_INTERVIEW_SCHEDULE;
                case RESCHEDULE -> AppConstants.EMAIL_TEMPLATE_INTERVIEWER_INTERVIEW_RESCHEDULE;
                case CANCEL -> AppConstants.EMAIL_TEMPLATE_INTERVIEWER_INTERVIEW_CANCEL;
            };
            mailService.sendEmailWithCalendarInvite(
                    candidate.getEmail(),
                    subject,
                    templateCandidate,
                    emailData,
                    calendarContent,
                    ""
            );
            interviewersEntitiesList.forEach(interviewer -> {
                mailService.sendEmailWithCalendarInvite(
                        interviewer.getEmail(),
                        subject,
                        templateInterviewer,
                        emailData,
                        calendarContent,
                        candidate.getFileUrl()
                );
            });




        } catch (Exception e) {
            log.error("Failed to send calendar invite", e);
            throw new RuntimeException("Failed to send interview calendar invite", e);
        }
    }

    private String buildCalendarContent(String meetingUid, Map<String, Object> emailData, CalendarAction action) {
        LocalDateTime startTime = (LocalDateTime) emailData.get(AppConstants.EMAIL_DATE_TIME);
        LocalDateTime endTime = startTime.plusHours(1);

        String candidateEmail = (String) emailData.get(AppConstants.EMAIL_DATA_CANDIDATE_EMAIL);
        String interviewerEmail = (String) emailData.get(AppConstants.EMAIL_DATA_INTERVIEWER_EMAIL);

        // Get the location from the emailData map. It can be null.
        String location = (String) emailData.get(AppConstants.EMAIL_DATA_LOCATION);

        String method, status, summary;
        int sequence;

        switch (action) {
            case SCHEDULE: {
                method = "REQUEST";
                status = "CONFIRMED";
                summary = "Interview with Candidate (Scheduled)";
                sequence = 0;
                break;
            }
            case RESCHEDULE: {
                method = "REQUEST";
                status = "CONFIRMED";
                summary = "Interview with Candidate (Rescheduled)";
                sequence = 1;
                break;
            }
            case CANCEL: {
                method = "CANCEL";
                status = "CANCELLED";
                summary = "Interview with Candidate (Cancelled)";
                sequence = 2;
                break;
            }
            default:
                throw new IllegalArgumentException("Invalid Calendar Action");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");

        // Use a StringBuilder for more control over the output
        StringBuilder calendarContent = new StringBuilder();
        calendarContent.append("BEGIN:VCALENDAR\r\n");
        calendarContent.append("METHOD:").append(method).append("\r\n");
        calendarContent.append("PRODID:-//Spring Boot Mail//EN\r\n");
        calendarContent.append("VERSION:2.0\r\n");
        calendarContent.append("BEGIN:VEVENT\r\n");
        calendarContent.append("DTSTAMP:").append(startTime.format(formatter)).append("\r\n");
        calendarContent.append("DTSTART:").append(startTime.format(formatter)).append("\r\n");
        calendarContent.append("DTEND:").append(endTime.format(formatter)).append("\r\n");
        calendarContent.append("SUMMARY:").append(summary).append("\r\n");
        calendarContent.append("UID:").append(meetingUid).append("\r\n");

        // ==================== CONDITIONAL LOGIC ====================
        // Only add the LOCATION line if the location string is not null and not empty.
        if (location != null && !location.trim().isEmpty()) {
            calendarContent.append("LOCATION:").append(location).append("\r\n");
        }
        // ==========================================================

        calendarContent.append("ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:mailto:").append(candidateEmail).append("\r\n");
        calendarContent.append("ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:mailto:").append(interviewerEmail).append("\r\n");
        calendarContent.append("ORGANIZER:mailto:spring.learn6@gmail.com\r\n");
        calendarContent.append("DESCRIPTION:Interview managed via Candidate Portal\r\n");
        calendarContent.append("SEQUENCE:").append(sequence).append("\r\n");
        calendarContent.append("STATUS:").append(status).append("\r\n");
        calendarContent.append("TRANSP:OPAQUE\r\n");
        calendarContent.append("END:VEVENT\r\n");
        calendarContent.append("END:VCALENDAR\r\n");

        return calendarContent.toString();
    }
}
