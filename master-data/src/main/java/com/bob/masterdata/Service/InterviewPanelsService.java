package com.bob.masterdata.Service;

import com.bob.db.entity.InterviewPanelMembersEntity;
import com.bob.db.entity.InterviewPanelsEntity;
import com.bob.db.repository.InterviewPanelMembersRepository;
import com.bob.db.repository.InterviewPanelsRepository;
import com.bob.masterdata.Model.InterviewPanelsModel;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class InterviewPanelsService {
    @Autowired
    private InterviewPanelsRepository interviewPanelsRepository;

    @Autowired
    private InterviewPanelMembersRepository interviewPanelMembersRepository;

    public InterviewPanelsModel createInterviewPanel(InterviewPanelsModel interviewPanelsModel) {
        List<InterviewPanelMembersEntity> interviewPanelMembersEntityList = new ArrayList<>();
        InterviewPanelsEntity interviewPanels = new InterviewPanelsEntity();

        interviewPanels.setPanelName(interviewPanelsModel.getPanelName());
        interviewPanels.setDescription(interviewPanelsModel.getPanelDescription());

        // Save the panel
        InterviewPanelsEntity interviewPanelsres = interviewPanelsRepository.save(interviewPanels);

        // Prepare members
        interviewPanelsModel.getInterviewerIds().forEach(interviewerId -> {
            InterviewPanelMembersEntity member = new InterviewPanelMembersEntity();
            member.setInterviewerId(interviewerId);
            member.setPanelId(interviewPanelsres.getPanelId());
            interviewPanelMembersEntityList.add(member);
        });

        // Save members
        interviewPanelMembersRepository.saveAll(interviewPanelMembersEntityList);

        // Prepare response model
        InterviewPanelsModel interviewPanelsModel1 = new InterviewPanelsModel();
        interviewPanelsModel1.setPanelId(interviewPanelsres.getPanelId());
        interviewPanelsModel1.setPanelName(interviewPanelsres.getPanelName());
        interviewPanelsModel1.setPanelDescription(interviewPanelsres.getDescription());
        interviewPanelsModel1.setInterviewerIds(interviewPanelsModel.getInterviewerIds());

        return interviewPanelsModel1;
    }

    public List<InterviewPanelsModel> getAllInterviewPanels() {
        List<InterviewPanelsEntity> interviewPanelsEntityList = interviewPanelsRepository.findAllByIsActiveTrue();
        List<InterviewPanelMembersEntity> interviewPanelMembersEntityList = interviewPanelMembersRepository.findAll();
        List<InterviewPanelsModel> interviewPanelsModelList = new ArrayList<>();

        interviewPanelsEntityList.forEach(interviewPanelsEntity -> {
            InterviewPanelsModel interviewPanelsModel = new InterviewPanelsModel();
            interviewPanelsModel.setPanelId(interviewPanelsEntity.getPanelId());
            interviewPanelsModel.setPanelName(interviewPanelsEntity.getPanelName());
            interviewPanelsModel.setPanelDescription(interviewPanelsEntity.getDescription());

            // Collect interviewerIds for this panel
            List<Long> interviewerIds = interviewPanelMembersEntityList.stream()
                    .filter(member -> member.getPanelId().equals(interviewPanelsEntity.getPanelId()))
                    .map(InterviewPanelMembersEntity::getInterviewerId)
                    .toList();

            interviewPanelsModel.setInterviewerIds(interviewerIds);

            // Add to result list
            interviewPanelsModelList.add(interviewPanelsModel);
        });

        return interviewPanelsModelList;
    }

//    public InterviewPanelsModel updateInterviewPanel(Long panel_id,InterviewPanelsModel interviewPanelsModel) {
//        InterviewPanelsEntity interviewPanelsEntity = interviewPanelsRepository.findById(panel_id).orElse(null);
//        List<InterviewPanelMembersEntity> interviewPanelMembersEntityList = interviewPanelMembersRepository.findAllByPanelId(interviewPanelsModel.getPanelId());
//        List<InterviewPanelMembersEntity> entityList = new  ArrayList<>();
//        if (interviewPanelsEntity != null) {
//            interviewPanelsEntity.setPanelName(interviewPanelsModel.getPanelName());
//            interviewPanelsEntity.setDescription(interviewPanelsModel.getPanelDescription());
//            interviewPanelsRepository.save(interviewPanelsEntity);
//
//            interviewPanelsModel.getInterviewerIds().forEach(interviewerId -> {
//                InterviewPanelMembersEntity member = new InterviewPanelMembersEntity();
//                member.setInterviewerId(interviewerId);
//                member.setPanelId(interviewPanelsEntity.getPanelId());
//                entityList.add(member);
//            });
//
//            interviewPanelMembersRepository.saveAll(entityList);
//            interviewPanelMembersRepository.deleteAllById(
//                    interviewPanelMembersEntityList.stream()
//                            .map(InterviewPanelMembersEntity::getPanelMemberId)
//                            .toList()
//            );
//
//        }
//        return interviewPanelsModel;
//    }

    @Transactional
    public InterviewPanelsModel updateInterviewPanel(Long panelId, InterviewPanelsModel interviewPanelsModel) {
        InterviewPanelsEntity interviewPanelsEntity = interviewPanelsRepository.findById(panelId)
                .orElseThrow(() -> new RuntimeException("Panel not found with id: " + panelId));

        // Update panel details
        interviewPanelsEntity.setPanelName(interviewPanelsModel.getPanelName());
        interviewPanelsEntity.setDescription(interviewPanelsModel.getPanelDescription());
        interviewPanelsRepository.save(interviewPanelsEntity);

        // Delete old members directly
        interviewPanelMembersRepository.deleteAllByPanelId(panelId);

        // Create new members
        List<InterviewPanelMembersEntity> newMembers = interviewPanelsModel.getInterviewerIds().stream()
                .map(interviewerId -> {
                    InterviewPanelMembersEntity member = new InterviewPanelMembersEntity();
                    member.setInterviewerId(interviewerId);
                    member.setPanelId(panelId);
                    return member;
                })
                .toList();

        interviewPanelMembersRepository.saveAll(newMembers);

        // Build response
        InterviewPanelsModel updatedModel = new InterviewPanelsModel();
        updatedModel.setPanelId(interviewPanelsEntity.getPanelId());
        updatedModel.setPanelName(interviewPanelsEntity.getPanelName());
        updatedModel.setPanelDescription(interviewPanelsEntity.getDescription());
        updatedModel.setInterviewerIds(interviewPanelsModel.getInterviewerIds());

        return updatedModel;
    }


    public void deleteInterviewPanel(Long panelId) {
        InterviewPanelsEntity interviewPanels = interviewPanelsRepository.findById(panelId)
                .orElseThrow(() -> new RuntimeException("Panel not found with id: " + panelId));
        interviewPanels.setIsActive(false);
        interviewPanelsRepository.save(interviewPanels);
    }

    public List<Long> activePanelNumbers() {
        List<InterviewPanelsEntity> interviewPanelsEntityList = interviewPanelsRepository.findAllByIsActiveTrue();
        List<Long> panelIds = interviewPanelsEntityList.stream().map(InterviewPanelsEntity::getPanelId).toList();
        List<InterviewPanelMembersEntity> interviewPanelMembersEntityList = interviewPanelMembersRepository.findAllByPanelIdIn(panelIds);

        return interviewPanelMembersEntityList.stream().map( InterviewPanelMembersEntity::getInterviewerId).toList();
    }


}
