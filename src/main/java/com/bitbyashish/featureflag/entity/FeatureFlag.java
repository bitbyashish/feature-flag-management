package com.bitbyashish.featureflag.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "feature_flags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeatureFlag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;  // e.g., "dark_mode"

    private String description;

    @Enumerated(EnumType.STRING)
    private FlagStatus status; // ON / OFF

    private Integer rolloutPercent; // e.g., 20 means 20% users

    @Enumerated(EnumType.STRING)
    private Environment environment; // DEV, STAGING, PROD

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "featureFlag", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FlagRule> rules;
}

