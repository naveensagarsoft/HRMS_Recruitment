package com.bob.candidateportal.Service;

import com.bob.candidateportal.util.Util;
import com.bob.db.entity.*;
import com.bob.db.repository.*;

import com.bob.candidateportal.model.InterviewResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CalendarService {

    @Autowired
    private InterviewsRepository interviewsRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private JobRequisitionsRepository jobRequisitionsRepository;

    @Autowired
    private PositionsRepository jobPositionsRepository;

    @Autowired
    private CandidateApplicationsRepository candidateApplicationsRepository;

    public List<InterviewResponse> getInterviewSchedulesBetween(long startTimestamp, long endTimestamp) {
        LocalDateTime startDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(startTimestamp), ZoneId.systemDefault());
        LocalDateTime endDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(endTimestamp), ZoneId.systemDefault());

        List<InterviewsEntity> interviewsEntityList = interviewsRepository.findByScheduledAtBetween(startDate, endDate);
        if (interviewsEntityList.isEmpty()) {
            return new ArrayList<>();
        }
        List<UUID> applicationIds= interviewsEntityList.stream().map(InterviewsEntity::getApplicationId).toList();

        List<CandidateApplicationsEntity> candidateApplications=candidateApplicationsRepository.findAllById(applicationIds);
        List<InterviewResponse> interviewResponseList = new ArrayList<>();


        //fetch all candidate IDs from the interviews
        List<UUID> candidateIds = candidateApplications.stream()

                .map(CandidateApplicationsEntity::getCandidateId)
                .toList();

        List<UUID> positionIds = candidateApplications.stream()
                .map(CandidateApplicationsEntity::getPositionId)
                .toList();

        //fetch all candidates from the candidate repository
        List<CandidatesEntity> candidatesList = candidateRepository.findAllById(candidateIds);

        //fetch all positions from the position repository

        List<PositionsEntity> positionsList = jobPositionsRepository.findByPositionIdInAndIsActive(positionIds,true);

        List<UUID> requisitionIds = positionsList.stream()
                .map(PositionsEntity::getRequisitionId)
                .toList();

//        List<JobRequisitionsEntity> jobRequisitionsList = jobRequisitionsRepository.findAllById(requisitionIds);

        List<JobRequisitionsEntity> jobRequisitionsList = jobRequisitionsRepository.findAllByRequisitionIdInAndIsactiveOrderByRequisitionCodeDesc(requisitionIds,1);


        interviewsEntityList.forEach(interview -> {
            CandidateApplicationsEntity candidateApplicationsEntity=candidateApplications.stream()
                    .filter(candidatesEntity -> candidatesEntity.getApplicationId().equals(interview.getApplicationId()))
                    .findFirst().orElse(null);

            CandidatesEntity candidate = candidatesList.stream()
                    .filter(c -> c.getCandidateId().equals(candidateApplicationsEntity.getCandidateId()))
                    .findFirst()
                    .orElse(null);

            PositionsEntity position = positionsList.stream()
                    .filter(p -> p.getPositionId().equals(candidateApplicationsEntity.getPositionId()))
                    .findFirst()
                    .orElse(null);

            JobRequisitionsEntity jobRequisition = jobRequisitionsList.stream()
                    .filter(j -> position != null && j.getRequisitionId().equals(position.getRequisitionId()))
                    .findFirst()
                    .orElse(null);

            if (candidate != null && position != null && jobRequisition != null) {
                InterviewResponse response = InterviewResponse.builder()
                        .interviewTime(interview.getScheduledAt() != null ? interview.getScheduledAt().toString() : null)
                        .interviewTitle(Util.nullcheckString(position.getPositionTitle()))
                        .candidateName(Util.nullcheckString(candidate.getFullName()))
                        .candidateSkill(Util.nullcheckString(candidate.getCurrentDesignation()))
                        .applicationStatus(Util.nullcheckString(interview.getStatus()))
//                        .interviewName(Util.nullcheckString(interview.getInterviewer()))
                        .requisitionCode(Util.nullcheckString(jobRequisition.getRequisitionCode()))
                        .build();
                interviewResponseList.add(response);
            }
        });

        return interviewResponseList;

    }
}