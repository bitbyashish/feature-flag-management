package com.bitbyashish.featureflag.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action; // CREATED, UPDATED, TOGGLED
    private String performedBy; // admin username/email
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "feature_flag_id")
    private FeatureFlag featureFlag;
}
