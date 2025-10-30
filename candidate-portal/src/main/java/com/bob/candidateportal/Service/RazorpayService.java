package com.bob.candidateportal.Service;

import com.bob.db.dto.RazorpayOrderDTO;
import com.bob.candidateportal.model.RazorpayOrdersResponse;
import com.bob.db.mapper.RazorpayOrderMapper;
import com.bob.db.repository.CandidatesRepository;
import com.bob.db.repository.JobRequisitionsRepository;
import com.bob.db.repository.PositionsRepository;
import com.bob.db.repository.RazorpayOrderRepository;
import com.bob.db.entity.CandidatesEntity;
import com.bob.db.entity.PositionsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RazorpayService {

    @Autowired
    private RazorpayOrderRepository razorpayOrderRepository;

    @Autowired
    private CandidatesRepository candidatesRepository;

    @Autowired
    private PositionsRepository positionsRepository;

    @Autowired
    private JobRequisitionsRepository jobRequisitionsRepository;

    @Autowired
    private RazorpayOrderMapper razorpayOrderMapper;

    public List<RazorpayOrdersResponse> getAllRazorpayOrders() {
        List<RazorpayOrderDTO> razorpayOrdersList = razorpayOrderRepository.findAll()
                .stream()
                .map(razorpayOrderMapper::toDto)
                .collect(Collectors.toList());

        // Fetch all candidates by IDs
        ArrayList<UUID> candidateUUIDs = new ArrayList<>();
        for (RazorpayOrderDTO dto : razorpayOrdersList) {
            try {
            if (dto.getCandidateId() != null) {
                candidateUUIDs.add(UUID.fromString(dto.getCandidateId()));
            }} catch (IllegalArgumentException ignored) { }
        }
        // Build a lookup map for candidates by UUID directly
        Map<UUID, CandidatesEntity> candidatesById = candidateUUIDs.isEmpty()
                ? Map.of()
                : candidatesRepository.findByCandidateIdIn(candidateUUIDs)
                    .stream()
                    .collect(Collectors.toMap(CandidatesEntity::getCandidateId, Function.identity(), (a, b) -> a));

        // Fetch all positions by IDs
        ArrayList<UUID> positionUUIDs = new ArrayList<>();
        for (RazorpayOrderDTO dto : razorpayOrdersList) {
            try {
                if (dto.getPositionId() != null) {
                    positionUUIDs.add(UUID.fromString(dto.getPositionId()));
                }
            } catch (IllegalArgumentException ignored) { }
        }
        // Build a lookup map for positions by UUID directly
        Map<UUID, PositionsEntity> positionsById = positionUUIDs.isEmpty()
                ? Map.of()
                : positionsRepository.findByPositionIdIn(positionUUIDs)
                    .stream()
                    .collect(Collectors.toMap(PositionsEntity::getPositionId, Function.identity(), (a, b) -> a));

        return razorpayOrdersList.stream()
                .map(dto -> {
                    RazorpayOrdersResponse r = new RazorpayOrdersResponse();
                    r.setRazorpayOrderDetails(dto);
                    // Populate candidate fields if available
                    try {
                        if (dto.getCandidateId() != null) {
                            CandidatesEntity c = candidatesById.get(UUID.fromString(dto.getCandidateId()));
                            if (c != null) {
                                r.setCandidateEmail(c.getEmail());
                                r.setCandidateName(c.getFullName());
                                r.setCandidateCurrentDesignation(c.getCurrentDesignation());
                            }
                        }
                    } catch (IllegalArgumentException ignored) { }
                    // Populate position fields if available
                    try {
                        if (dto.getPositionId() != null) {
                            PositionsEntity p = positionsById.get(UUID.fromString(dto.getPositionId()));
                            if (p != null) {
                                RazorpayOrderDTO details = r.getRazorpayOrderDetails();
                                details.setRequisitionCode(jobRequisitionsRepository.findById(p.getRequisitionId()).orElse(null).getRequisitionCode());
                                r.setRazorpayOrderDetails(details);
                                r.setPositionTitle(p.getPositionTitle());
                                r.setPositionDescription(p.getDescription());
                            }
                        }
                    } catch (IllegalArgumentException ignored) { }
                    return r;
                })
                .collect(Collectors.toList());
    }

}
