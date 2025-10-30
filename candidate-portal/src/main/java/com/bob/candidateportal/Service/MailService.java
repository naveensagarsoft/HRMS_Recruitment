package com.bob.candidateportal.Service;

import com.bob.candidateportal.util.AppConstants;
import jakarta.activation.URLDataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMultipart;

import jakarta.activation.DataHandler;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

@Slf4j
@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public String sendSimpleEmail(String toEmail, String subject, String template, Map<String, Object> variables) {
        try {
            String meetingUid = toEmail + "_" + (variables.get(AppConstants.EMAIL_DATA_INTERVIEWER_EMAIL) != null ?
                    variables.get(AppConstants.EMAIL_DATA_INTERVIEWER_EMAIL).toString() : "unknown") + "_" + variables.get(AppConstants.EMAIL_DATA_CANDIDATE_APPLICATION_ID);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            Context context = new Context();
            context.setVariables(variables);
            if (variables.get(AppConstants.EMAIL_DATA_STATUS).equals(AppConstants.CAND_APP_STATUS_SCHEDULED)) {

                String htmlContent = templateEngine.process(template, context);

                helper.setFrom("spring.learn6@gmail.com", "Do Not Reply");
                helper.setTo(toEmail);
                helper.setSubject(subject);

                // HTML content
                MimeBodyPart htmlPart = new MimeBodyPart();
                htmlPart.setContent(htmlContent, "text/html; charset=utf-8");
                LocalDateTime startDate = LocalDateTime.of((LocalDate) variables.get(AppConstants.EMAIL_DATA_DATE), (LocalTime) variables.get(AppConstants.EMAIL_DATA_TIME));
                LocalDateTime endDate = startDate.plusHours(1);
                String calendarContent =
                        "BEGIN:VCALENDAR\n" +
                                "METHOD:REQUEST\n" +
                                "PRODID:-//Spring Boot Mail//EN\n" +
                                "VERSION:2.0\n" +
                                "BEGIN:VEVENT\n" +
                                "DTSTAMP:" + startDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")) + "\n" +
                                "DTSTART:" + startDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")) + "\n" +
                                "DTEND:" + endDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")) + "\n" +
                                "SUMMARY:Interview with Candidate\n" +
                                "UID:" + meetingUid + "\n" +
                                "ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:mailto:" + toEmail + "\n" +
                                "ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:mailto:" + variables.get(AppConstants.EMAIL_DATA_INTERVIEWER_EMAIL) + "\n" +
                                "ORGANIZER:mailto:spring.learn6@gmail.com\n" +
//                                "LOCATION:"  + variables.get(AppConstants.EMAIL_DATA_LINK) + "\n" +
                                "DESCRIPTION:Interview scheduled via Candidate Portal, Meeting Link-" + variables.get(AppConstants.EMAIL_DATA_LINK) + "\n" +
                                "SEQUENCE:0\n" +
                                "STATUS:CONFIRMED\n" +
                                "TRANSP:OPAQUE\n" +
                                "END:VEVENT\n" +
                                "END:VCALENDAR";

                // Calendar content
                MimeBodyPart calendarPart = new MimeBodyPart();
                calendarPart.setContent(calendarContent, "text/calendar;method=REQUEST;charset=UTF-8");

                // Combine content
                MimeMultipart multipart = new MimeMultipart("alternative");
                multipart.addBodyPart(htmlPart);
                multipart.addBodyPart(calendarPart);

                message.setContent(multipart);

                message.setHeader("Content-Class", "urn:content-classes:calendarmessage");
                message.setHeader("Content-ID", "calendar_message");

                mailSender.send(message);
                return "Mail Sent!";
            } else if (variables.get(AppConstants.EMAIL_DATA_STATUS).equals(AppConstants.CAND_APP_STATUS_RESCHEDULED)) {

                String htmlContent = templateEngine.process(template, context);

                helper.setFrom("spring.learn6@gmail.com", "Do Not Reply");
                helper.setTo(toEmail);
                helper.setSubject(subject);

                // HTML content
                MimeBodyPart htmlPart = new MimeBodyPart();
                htmlPart.setContent(htmlContent, "text/html; charset=utf-8");
                LocalDateTime startDate = LocalDateTime.of((LocalDate) variables.get(AppConstants.EMAIL_DATA_DATE), (LocalTime) variables.get(AppConstants.EMAIL_DATA_TIME));
                LocalDateTime endDate = startDate.plusHours(1);
                String calendarContent =
                        "BEGIN:VCALENDAR\n" +
                                "METHOD:REQUEST\n" +
                                "PRODID:-//Spring Boot Mail//EN\n" +
                                "VERSION:2.0\n" +
                                "BEGIN:VEVENT\n" +
                                "DTSTAMP:" + startDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")) + "\n" +
                                "DTSTART:" + startDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")) + "\n" +
                                "DTEND:" + endDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")) + "\n" +
                                "SUMMARY:Interview with Candidate (Rescheduled)\n" +
                                "UID:" + meetingUid + "\n" +
                                "ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:mailto:" + toEmail + "\n" +
                                "ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:mailto:" + variables.get(AppConstants.EMAIL_DATA_INTERVIEWER_EMAIL) + "\n" +
                                "ORGANIZER:mailto:spring.learn6@gmail.com\n" +
//                                "LOCATION:"  + variables.get(AppConstants.EMAIL_DATA_LINK) + "\n" +
                                "DESCRIPTION:Interview rescheduled via Candidate Portal, Meeting Link-" + variables.get(AppConstants.EMAIL_DATA_LINK) + "\n" +
                                "SEQUENCE:1\n" +
                                "STATUS:CONFIRMED\n" +
                                "TRANSP:OPAQUE\n" +
                                "END:VEVENT\n" +
                                "END:VCALENDAR";

                // Calendar content
                MimeBodyPart calendarPart = new MimeBodyPart();
                calendarPart.setContent(calendarContent, "text/calendar;method=REQUEST;charset=UTF-8");

                // Combine content
                MimeMultipart multipart = new MimeMultipart("alternative");
                multipart.addBodyPart(htmlPart);
                multipart.addBodyPart(calendarPart);

                message.setContent(multipart);

                message.setHeader("Content-Class", "urn:content-classes:calendarmessage");
                message.setHeader("Content-ID", "calendar_message");

                mailSender.send(message);
                return "Mail Sent!";
            } else if (variables.get(AppConstants.EMAIL_DATA_STATUS).equals(AppConstants.CAND_APP_STATUS_CANCELLED)) {

                String htmlContent = templateEngine.process(template, context);

                helper.setFrom("spring.learn6@gmail.com", "Do Not Reply");
                helper.setTo(toEmail);
                helper.setSubject(subject);

                // HTML content
                MimeBodyPart htmlPart = new MimeBodyPart();
                htmlPart.setContent(htmlContent, "text/html; charset=utf-8");
                LocalDateTime prevStartTime = (LocalDateTime) variables.get(AppConstants.PREVIOUS_INTERVIEW_TIMESTAMP);
                LocalDateTime prevEndTime = prevStartTime.plusHours(1);
                String calendarContent =
                        "BEGIN:VCALENDAR\n" +
                                "METHOD:CANCEL\n" +
                                "PRODID:-//Spring Boot Mail//EN\n" +
                                "VERSION:2.0\n" +
                                "BEGIN:VEVENT\n" +
                                "DTSTAMP:" + prevStartTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")) + "\n" +
                                "DTSTART:" + prevStartTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")) + "\n" +
                                "DTEND:" + prevEndTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")) + "\n" +
                                "SUMMARY:Interview with Candidate (Cancelled)\n" +
                                "UID:" + meetingUid + "\n" +
                                "ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:mailto:" + toEmail + "\n" +
                                "ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:mailto:" + variables.get(AppConstants.EMAIL_DATA_INTERVIEWER_EMAIL) + "\n" +
                                "ORGANIZER:mailto:spring.learn6@gmail.com\n" +
//                                "LOCATION:"  + variables.get(AppConstants.EMAIL_DATA_LINK) + "\n" +
                                "DESCRIPTION:Interview cancelled via Candidate Portal\n" +
                                "SEQUENCE:2\n" +
                                "STATUS:CANCELLED\n" +
                                "TRANSP:OPAQUE\n" +
                                "END:VEVENT\n" +
                                "END:VCALENDAR";

                // Calendar content
                MimeBodyPart calendarPart = new MimeBodyPart();
                calendarPart.setContent(calendarContent, "text/calendar;method=REQUEST;charset=UTF-8");

                // Combine content
                MimeMultipart multipart = new MimeMultipart("alternative");
                multipart.addBodyPart(htmlPart);
                multipart.addBodyPart(calendarPart);

                message.setContent(multipart);

                message.setHeader("Content-Class", "urn:content-classes:calendarmessage");
                message.setHeader("Content-ID", "calendar_message");

                mailSender.send(message);
                return "Mail Sent!";
            } else {
                String htmlContent = templateEngine.process(template, context);

                helper.setFrom("spring.learn6@gmail.com", "Do Not Reply");
                helper.setTo(toEmail);
                helper.setSubject(subject);
                helper.setText(htmlContent, true);

                mailSender.send(message);
                return "Mail Sent!";
            }
        } catch (Exception e) {
            return "Error while sending email: " + e.getMessage();
        }
    }


    public String sendEmailWithAttachment(String toEmail, String subject, String path, Map<String, Object> variables, String template) throws MessagingException, UnsupportedEncodingException {
        try {

            String meetingUid = (variables.get(AppConstants.EMAIL_DATA_CANDIDATE_EMAIL) != null ? variables.get(AppConstants.EMAIL_DATA_CANDIDATE_EMAIL).toString() : "unknown") + "_" + toEmail + "_" + variables.get(AppConstants.EMAIL_DATA_CANDIDATE_APPLICATION_ID);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            Context context = new Context();
            context.setVariables(variables);
            if (variables.get(AppConstants.EMAIL_DATA_STATUS).equals(AppConstants.CAND_APP_STATUS_SCHEDULED)) {

                String htmlContent = templateEngine.process(template, context);

                helper.setFrom("spring.learn6@gmail.com", "Do Not Reply");
                helper.setTo(toEmail);
                helper.setSubject(subject);

                // HTML content
                MimeBodyPart htmlPart = new MimeBodyPart();
                htmlPart.setContent(htmlContent, "text/html; charset=utf-8");
                LocalDateTime startDate = LocalDateTime.of((LocalDate) variables.get(AppConstants.EMAIL_DATA_DATE), (LocalTime) variables.get(AppConstants.EMAIL_DATA_TIME));
                LocalDateTime endDate = startDate.plusHours(1);
                String calendarContent =
                        "BEGIN:VCALENDAR\n" +
                                "METHOD:REQUEST\n" +
                                "PRODID:-//Spring Boot Mail//EN\n" +
                                "VERSION:2.0\n" +
                                "BEGIN:VEVENT\n" +
                                "DTSTAMP:" + startDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")) + "\n" +
                                "DTSTART:" + startDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")) + "\n" +
                                "DTEND:" + endDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")) + "\n" +
                                "SUMMARY:Interview with Candidate\n" +
                                "UID:" + meetingUid + "\n" +
                                "ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:mailto:" + toEmail + "\n" +
                                "ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:mailto:" + variables.get(AppConstants.EMAIL_DATA_CANDIDATE_EMAIL) + "\n" +
                                "ORGANIZER:mailto:spring.learn6@gmail.com\n" +
//                                "LOCATION:"  + variables.get(AppConstants.EMAIL_DATA_LINK) + "\n" +
                                "DESCRIPTION:Interview scheduled via Candidate Portal, Meeting Link-" + variables.get(AppConstants.EMAIL_DATA_LINK) + "\n" +
                                "SEQUENCE:0\n" +
                                "STATUS:CONFIRMED\n" +
                                "TRANSP:OPAQUE\n" +
                                "END:VEVENT\n" +
                                "END:VCALENDAR";

                // Calendar content
                MimeBodyPart calendarPart = new MimeBodyPart();
                calendarPart.setContent(calendarContent, "text/calendar;method=REQUEST;charset=UTF-8");

                // Combine HTML and calendar content
                MimeMultipart alternative = new MimeMultipart("alternative");
                alternative.addBodyPart(htmlPart);
                alternative.addBodyPart(calendarPart);

                MimeBodyPart alternativeBodyPart = new MimeBodyPart();
                alternativeBodyPart.setContent(alternative);

                // PDF attachment using UrlResource
                UrlResource pdfResource = new UrlResource(path);
                MimeBodyPart attachmentPart = new MimeBodyPart();
                attachmentPart.setDataHandler(new DataHandler(new URLDataSource(pdfResource.getURL())));
                attachmentPart.setFileName(getFileNameFromPath(path));

                // Combine all into mixed
                MimeMultipart mixed = new MimeMultipart("mixed");
                mixed.addBodyPart(alternativeBodyPart);
                mixed.addBodyPart(attachmentPart);

                message.setContent(mixed);

                message.setHeader("Content-Class", "urn:content-classes:calendarmessage");
                message.setHeader("Content-ID", "calendar_message");

                mailSender.send(message);
                return "Mail Sent with attachment!";

            } else if (variables.get(AppConstants.EMAIL_DATA_STATUS).equals(AppConstants.CAND_APP_STATUS_RESCHEDULED)) {

                String htmlContent = templateEngine.process(template, context);

                helper.setFrom("spring.learn6@gmail.com", "Do Not Reply");
                helper.setTo(toEmail);
                helper.setSubject(subject);

                // HTML content
                MimeBodyPart htmlPart = new MimeBodyPart();
                htmlPart.setContent(htmlContent, "text/html; charset=utf-8");
                LocalDateTime startDate = LocalDateTime.of((LocalDate) variables.get(AppConstants.EMAIL_DATA_DATE), (LocalTime) variables.get(AppConstants.EMAIL_DATA_TIME));
                LocalDateTime endDate = startDate.plusHours(1);
                String calendarContent =
                        "BEGIN:VCALENDAR\n" +
                                "METHOD:REQUEST\n" +
                                "PRODID:-//Spring Boot Mail//EN\n" +
                                "VERSION:2.0\n" +
                                "BEGIN:VEVENT\n" +
                                "DTSTAMP:" + startDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")) + "\n" +
                                "DTSTART:" + startDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")) + "\n" +
                                "DTEND:" + endDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")) + "\n" +
                                "SUMMARY:Interview with Candidate (Rescheduled)\n" +
                                "UID:" + meetingUid + "\n" +
                                "ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:mailto:" + toEmail + "\n" +
                                "ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:mailto:" + variables.get(AppConstants.EMAIL_DATA_CANDIDATE_EMAIL) + "\n" +
                                "ORGANIZER:mailto:spring.learn6@gmail.com\n" +
//                                "LOCATION:"  + variables.get(AppConstants.EMAIL_DATA_LINK) + "\n" +
                                "DESCRIPTION:Interview rescheduled via Candidate Portal, Meeting Link-" + variables.get(AppConstants.EMAIL_DATA_LINK) + "\n" +
                                "SEQUENCE:1\n" +
                                "STATUS:CONFIRMED\n" +
                                "TRANSP:OPAQUE\n" +
                                "END:VEVENT\n" +
                                "END:VCALENDAR";

                // Calendar content
                MimeBodyPart calendarPart = new MimeBodyPart();
                calendarPart.setContent(calendarContent, "text/calendar;method=REQUEST;charset=UTF-8");

                // Combine HTML and calendar content
                MimeMultipart alternative = new MimeMultipart("alternative");
                alternative.addBodyPart(htmlPart);
                alternative.addBodyPart(calendarPart);

                MimeBodyPart alternativeBodyPart = new MimeBodyPart();
                alternativeBodyPart.setContent(alternative);

                // PDF attachment using UrlResource
                UrlResource pdfResource = new UrlResource(path);
                MimeBodyPart attachmentPart = new MimeBodyPart();
                attachmentPart.setDataHandler(new DataHandler(new URLDataSource(pdfResource.getURL())));
                attachmentPart.setFileName(getFileNameFromPath(path));

                // Combine all into mixed
                MimeMultipart mixed = new MimeMultipart("mixed");
                mixed.addBodyPart(alternativeBodyPart);
                mixed.addBodyPart(attachmentPart);

                message.setContent(mixed);

                message.setHeader("Content-Class", "urn:content-classes:calendarmessage");
                message.setHeader("Content-ID", "calendar_message");

                mailSender.send(message);
                return "Mail Sent with attachment!";

            } else if (variables.get(AppConstants.EMAIL_DATA_STATUS).equals(AppConstants.CAND_APP_STATUS_CANCELLED)) {

                String htmlContent = templateEngine.process(template, context);

                helper.setFrom("spring.learn6@gmail.com", "Do Not Reply");
                helper.setTo(toEmail);
                helper.setSubject(subject);

                // HTML content
                MimeBodyPart htmlPart = new MimeBodyPart();
                htmlPart.setContent(htmlContent, "text/html; charset=utf-8");
                LocalDateTime prevStartTime = (LocalDateTime) variables.get(AppConstants.PREVIOUS_INTERVIEW_TIMESTAMP);
                LocalDateTime prevEndTime = prevStartTime.plusHours(1);
                String calendarContent =
                        "BEGIN:VCALENDAR\n" +
                                "METHOD:CANCEL\n" +
                                "PRODID:-//Spring Boot Mail//EN\n" +
                                "VERSION:2.0\n" +
                                "BEGIN:VEVENT\n" +
                                "DTSTAMP:" + prevStartTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")) + "\n" +
                                "DTSTART:" + prevStartTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")) + "\n" +
                                "DTEND:" + prevEndTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")) + "\n" +
                                "SUMMARY:Interview with Candidate (Cancelled)\n" +
                                "UID:" + meetingUid + "\n" +
                                "ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:mailto:" + toEmail + "\n" +
                                "ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:mailto:" + variables.get(AppConstants.EMAIL_DATA_CANDIDATE_EMAIL) + "\n" +
                                "ORGANIZER:mailto:spring.learn6@gmail.com\n" +
//                                "LOCATION:"  + variables.get(AppConstants.EMAIL_DATA_LINK) + "\n" +
                                "DESCRIPTION:Interview cancelled via Candidate Portal\n" +
                                "SEQUENCE:2\n" +
                                "STATUS:CANCELLED\n" +
                                "TRANSP:OPAQUE\n" +
                                "END:VEVENT\n" +
                                "END:VCALENDAR";

                // Calendar content
                MimeBodyPart calendarPart = new MimeBodyPart();
                calendarPart.setContent(calendarContent, "text/calendar;method=REQUEST;charset=UTF-8");

                // Combine HTML and calendar content
                MimeMultipart alternative = new MimeMultipart("alternative");
                alternative.addBodyPart(htmlPart);
                alternative.addBodyPart(calendarPart);

                MimeBodyPart alternativeBodyPart = new MimeBodyPart();
                alternativeBodyPart.setContent(alternative);

                // PDF attachment using UrlResource
                UrlResource pdfResource = new UrlResource(path);
                MimeBodyPart attachmentPart = new MimeBodyPart();
                attachmentPart.setDataHandler(new DataHandler(new URLDataSource(pdfResource.getURL())));
                attachmentPart.setFileName(getFileNameFromPath(path));

                // Combine all into mixed
                MimeMultipart mixed = new MimeMultipart("mixed");
                mixed.addBodyPart(alternativeBodyPart);
                mixed.addBodyPart(attachmentPart);

                message.setContent(mixed);

                message.setHeader("Content-Class", "urn:content-classes:calendarmessage");
                message.setHeader("Content-ID", "calendar_message");

                mailSender.send(message);
                return "Mail Sent with attachment!";

            } else {

                String htmlContent = templateEngine.process(template, context);
                helper.setFrom("spring.learn6@gmail.com", "Do Not Reply"); //TODO: before moving to prod fix this
                helper.setTo(toEmail);
                helper.setSubject(subject);


                helper.setText(htmlContent, true);


                UrlResource pdfResource = new UrlResource(path);

                helper.addAttachment(getFileNameFromPath(path), pdfResource);
                mailSender.send(message);

                return "Mail Sent with attachment!";

            }
        } catch (Exception e) {
            return "Error sending email due to" + e.getMessage();
        }
    }

    private String getFileNameFromPath(String path) {
        if (path == null || path.isEmpty()) {
            return "attachment";
        }
        String[] parts = path.split("/");
        return parts[parts.length - 1];
    }

//    @Async
//    public void sendEmailWithCalendarInvite(
//            String toEmail, String subject, String template, Map<String, Object> emailData, String calendarContent , String filePath) {
//        try {
//            MimeMessage message = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//            Context context = new Context();
//            context.setVariables(emailData);
//            String htmlContent = templateEngine.process(template, context);
//
//            helper.setFrom("spring.learn6@gmail.com", "Do Not Reply");
//            helper.setTo(toEmail);
//            helper.setSubject(subject);
//
//            // HTML part
//            MimeBodyPart htmlPart = new MimeBodyPart();
//            htmlPart.setContent(htmlContent, "text/html; charset=utf-8");
//
//            // Calendar part
//            MimeBodyPart calendarPart = new MimeBodyPart();
//            calendarPart.setContent(calendarContent, "text/calendar;method=REQUEST;charset=UTF-8");
//
//            // Combine
//            MimeMultipart multipart = new MimeMultipart("alternative");
//            multipart.addBodyPart(htmlPart);
//            multipart.addBodyPart(calendarPart);
//
//            message.setContent(multipart);
//            message.setHeader("Content-Class", "urn:content-classes:calendarmessage");
//            message.setHeader("Content-ID", "calendar_message");
//
//            // adding path attachment if available
//            if(filePath==null || filePath.isEmpty()){
//                try {
//                    UrlResource pdfResource = new UrlResource(filePath);
//                    MimeBodyPart attachmentPart = new MimeBodyPart();
//                    attachmentPart.setDataHandler(new DataHandler(new URLDataSource(pdfResource.getURL())));
//                    attachmentPart.setFileName(getFileNameFromPath(filePath));
//                }catch (Exception e){
//                    log.error("Error in attaching file to email",e.getMessage());
//                }
//            }
//            mailSender.send(message);
//        } catch (MessagingException | UnsupportedEncodingException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @Async
    public void sendEmailWithCalendarInvite(
            String toEmail, String subject, String template, Map<String, Object> emailData, String calendarContent, String filePath) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            Context context = new Context();
            context.setVariables(emailData);
            String htmlContent = templateEngine.process(template, context);

            helper.setFrom("spring.learn6@gmail.com", "Do Not Reply");
            helper.setTo(toEmail);
            helper.setSubject(subject);

            // 1. Use "mixed" for the main container to hold both the body and the attachment.
            MimeMultipart multipart = new MimeMultipart("mixed");

            // --- Create the Body Part (which itself contains the HTML and Calendar alternatives) ---
            MimeBodyPart bodyPart = new MimeBodyPart();

            // HTML part
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlContent, "text/html; charset=utf-8");

            // Calendar part
            MimeBodyPart calendarPart = new MimeBodyPart();
            calendarPart.setContent(calendarContent, "text/calendar;method=REQUEST;charset=UTF-8");

            // Combine HTML and Calendar into an "alternative" multipart
            MimeMultipart alternativeMultipart = new MimeMultipart("alternative");
            alternativeMultipart.addBodyPart(htmlPart);
            alternativeMultipart.addBodyPart(calendarPart);

            // Set the "alternative" multipart as the content of the main body part
            bodyPart.setContent(alternativeMultipart);

            // Add the main body part to the "mixed" multipart
            multipart.addBodyPart(bodyPart);

            // --- Create and Add the Attachment Part (conditionally) ---

            // 2. Correct the condition to check if the file path EXISTS.
            if (filePath != null && !filePath.isEmpty()) {
                try {
                    UrlResource pdfResource = new UrlResource(filePath);
                    MimeBodyPart attachmentPart = new MimeBodyPart();
                    attachmentPart.setDataHandler(new DataHandler(new URLDataSource(pdfResource.getURL())));
                    attachmentPart.setFileName(getFileNameFromPath(filePath));

                    // 3. Add the attachmentPart to the main "mixed" multipart.
                    multipart.addBodyPart(attachmentPart);
                } catch (Exception e) {
                    log.error("Error in attaching file to email", e.getMessage());
                }
            }

            // Set the final multipart content to the message
            message.setContent(multipart);
            message.setHeader("Content-Class", "urn:content-classes:calendarmessage");

            mailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}

