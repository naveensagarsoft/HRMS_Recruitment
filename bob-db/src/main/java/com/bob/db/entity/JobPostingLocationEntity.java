package com.bob.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "job_posting_location", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobPostingLocationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "posting_location_id", nullable = false)
    private Integer postingLocationId ;

    @Column(name = "position_id", nullable = false)
    private UUID positionId;
    @Column(name = "dept_id", nullable = false)
    private Integer deptId;

    @Column(name = "location_id", nullable = false)
    private Long locationId;

    @Column(name = "country_id")
    private Long countryId;

    @Column(name = "state_id")
    private Long stateId;

    @Column(name = "city_id")
    private Long cityId;


}
