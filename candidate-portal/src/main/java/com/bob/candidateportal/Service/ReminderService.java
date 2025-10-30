package com.bob.candidateportal.Service;

import com.bob.candidateportal.util.AppConstants;
import com.bob.db.entity.CandidateApplicationsEntity;
import com.bob.db.entity.CandidatesEntity;
import com.bob.db.entity.InterviewsEntity;
import com.bob.db.repository.CandidateApplicationsRepository;
import com.bob.db.repository.CandidateRepository;
import com.bob.db.repository.InterviewsRepository;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import jakarta.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@EnableAsync
public class ReminderService {

    private static final Logger logger = LoggerFactory.getLogger(ReminderService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private InterviewsRepository interviewsRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private CandidateApplicationsRepository candidateApplicationsRepository;

    @Value("${spring.mail.username}")
    private String hostMail;

    //Schedule it on morning 6AM
    @Scheduled(cron = "0 0 6 * * ?", zone = "Asia/Kolkata")
    public void sendMailReminders() {
        logger.info("Starting reminder email job.");
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusDays(3);

        // get interviews by date and status
        List<String> relevantStatuses = Arrays.asList(AppConstants.CAND_APP_STATUS_SCHEDULED, AppConstants.CAND_APP_STATUS_RESCHEDULED,AppConstants.CAND_APP_STATUS_SELECTED_FOR_NEXT_ROUND);
        List<InterviewsEntity> interviews = interviewsRepository.findByScheduledAtBetweenAndStatusIn(startDate, endDate, relevantStatuses);

        if (interviews.isEmpty()) {
            logger.info("No upcoming interviews found for reminders. Job finished.");
            return;
        }
        logger.info("Found {} interviews for reminder emails.", interviews.size());

        // Get all unique application IDs from the interviews
        Set<UUID> applicationIds = interviews.stream()
                .map(InterviewsEntity::getApplicationId)
                .collect(Collectors.toSet());

        // Fetch all candidate applications in one query and map them by application ID
        Map<UUID, CandidateApplicationsEntity> applicationMap = candidateApplicationsRepository.findAllById(applicationIds).stream()
                .collect(Collectors.toMap(CandidateApplicationsEntity::getApplicationId, app -> app));


        // Get all unique candidate IDs from the applications
        Set<UUID> candidateIds = applicationMap.values().stream()
                .map(CandidateApplicationsEntity::getCandidateId)
                .collect(Collectors.toSet());

        // Fetch all active candidates in one query and map them by candidate ID
        Map<UUID, CandidatesEntity> candidateMap = candidateRepository.findByCandidateIdInAndIsActiveTrue(candidateIds.stream().toList()).stream()
                .collect(Collectors.toMap(CandidatesEntity::getCandidateId, c -> c));

        // Send async emails using the pre-fetched data
        interviews.forEach(interview -> {
            CandidateApplicationsEntity application = applicationMap.get(interview.getApplicationId());
            if (application == null) {
                logger.warn("Could not find application for interview ID: {}", interview.getInterviewId());
                return;
            }

            CandidatesEntity candidate = candidateMap.get(application.getCandidateId());
            if (candidate != null && candidate.getEmail() != null) {
                sendReminderMailAsync(candidate, interview,application);
            } else {
                logger.warn("Could not find active candidate or email for interview ID: {}", interview.getInterviewId());
            }
        });
        logger.info("Finished processing reminder emails job.");
    }

    @Async
//    @Retryable(value={MessagingException.class, UnsupportedEncodingException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
    public void sendReminderMailAsync(CandidatesEntity candidate, InterviewsEntity interview,CandidateApplicationsEntity candidateApplicationsEntity) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(hostMail, "Do Not Reply");
            helper.setTo(candidate.getEmail());
            helper.setSubject("Interview Reminder");

            long daysBetween = java.time.Duration.between(LocalDateTime.now().toLocalDate().atStartOfDay(),
                            interview.getScheduledAt().toLocalDate().atStartOfDay())
                    .toDays();

            if (daysBetween < 0 || daysBetween > 2) {
                logger.warn("Skipping email for interview ID {} as it's not within the 0-2 day reminder window.", interview.getInterviewId());
                return;
            }

            Context context = new Context();
            context.setVariable("name", candidate.getFullName());
            context.setVariable("time", interview.getScheduledAt().toLocalTime());
            context.setVariable("date", interview.getScheduledAt().toLocalDate());
            context.setVariable("position", candidateApplicationsEntity.getPosition().getPositionTitle());

            String templateName;
            if (daysBetween == 0) {
                templateName = "InterviewToday";
            } else if (daysBetween == 1) {
                templateName = "InterviewTomorrow";
            } else { // daysBetween == 2
                templateName = "InterviewDayAfter";
            }

            String htmlContent = templateEngine.process(templateName, context);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
            logger.info("Successfully sent reminder email to {} for interview ID {}", candidate.getEmail(), interview.getInterviewId());
        } catch (MessagingException | UnsupportedEncodingException e) {
            logger.error("Failed to send reminder email to {} for interview ID {}: {}", candidate.getEmail(), interview.getInterviewId(), e.getMessage());
        }
    }


}
