package com.bob.db.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class JobVacancyDto {
    private Integer jobVacancyId;
    private UUID positionId;
    private Integer specialCatId;
    private Integer reservationCatId;
    private Long locationId;
    private Integer noOfVacancies;
}
