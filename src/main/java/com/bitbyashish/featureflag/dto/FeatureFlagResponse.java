package com.bitbyashish.featureflag.dto;

import com.bitbyashish.featureflag.entity.Environment;
import com.bitbyashish.featureflag.entity.FlagStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class FeatureFlagResponse {
    private Long id;
    private String name;
    private String description;
    private Environment environment;
    private FlagStatus status;
    private Integer rolloutPercent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<FlagRuleResponse> rules;
}

