package com.bob.db.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "interview_panel_members", schema = "public")
@EntityListeners(AuditingEntityListener.class)
public class InterviewPanelMembersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "panel_member_id")
    private Long panelMemberId;

    @Column(name = "panel_id")
    private Long panelId;

    @Column(name = "interviewer_id")
    private Long interviewerId;

    @Column(name = "role_in_panel")
    private String roleInPanel;

    @Column(name = "is_primary")
    private Boolean isPrimary = false;

//    @Column(name = "added_date")
//    @CreationTimestamp
//    private LocalDateTime addedDate;

}
