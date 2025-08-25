package com.bitbyashish.featureflag.dto;

import com.bitbyashish.featureflag.entity.Environment;
import com.bitbyashish.featureflag.entity.FlagStatus;
import lombok.Data;

import java.util.List;

@Data
public class FeatureFlagRequest {
    private String name;
    private String description;
    private Environment environment;
    private FlagStatus status;
    private Integer rolloutPercent;
    private List<String> userGroups; // to create rules
}
