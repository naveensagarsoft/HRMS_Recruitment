package com.bob.db.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "candidate_nationality", schema = "public")
@Data
public class CandidateNationalityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "candidate_nationality_id")
    private int candidateNationalityId;

    @Column(name = "position_id")
    private UUID positionId;

    @Column(name = "country_id")
    private int countryId;

}
