package com.bitbyashish.featureflag.dto;

import com.bitbyashish.featureflag.entity.Environment;
import com.bitbyashish.featureflag.entity.FlagStatus;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class FeatureFlagRequest {
    @NotBlank(message = "Flag name is required")
    private String name;
    
    @Size(max = 255, message = "Description must be at most 255 characters")
    private String description;

    @NotNull(message = "Environment is required")
    private Environment environment;

    @NotNull(message = "Status is required")
    private FlagStatus status;

    @Min(value = 0, message = "Rollout percentage must be between 0 and 100")
    @Max(value = 100, message = "Rollout percentage must be between 0 and 100")
    private Integer rolloutPercent;
    private List<String> userGroups; // to create rules
}
