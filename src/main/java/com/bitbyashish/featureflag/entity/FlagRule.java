package com.bitbyashish.featureflag.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "flag_rules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlagRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userGroup; // e.g., "beta_testers"

    @Column(columnDefinition = "TEXT")
    private String condition; // JSON or custom condition string

    @ManyToOne
    @JoinColumn(name = "feature_flag_id")
    private FeatureFlag featureFlag;
}

