package com.bitbyashish.featureflag.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bitbyashish.featureflag.dto.FeatureFlagRequest;
import com.bitbyashish.featureflag.dto.FeatureFlagResponse;
import com.bitbyashish.featureflag.dto.FlagRuleResponse;
import com.bitbyashish.featureflag.entity.Environment;
import com.bitbyashish.featureflag.entity.FeatureFlag;
import com.bitbyashish.featureflag.entity.FlagRule;
import com.bitbyashish.featureflag.entity.FlagStatus;
import com.bitbyashish.featureflag.exception.ResourceNotFoundException;
import com.bitbyashish.featureflag.repository.FeatureFlagRepository;
import com.bitbyashish.featureflag.repository.FlagRuleRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FeatureFlagService {

    private final FeatureFlagRepository featureFlagRepository;
    private final FlagRuleRepository flagRuleRepository;

    // -------------------------
    // Create Feature Flag
    // -------------------------
    public FeatureFlagResponse createFlag(FeatureFlagRequest request) {
        FeatureFlag flag = FeatureFlag.builder()
                .name(request.getName())
                .description(request.getDescription())
                .environment(request.getEnvironment())
                .status(request.getStatus() != null ? request.getStatus() : FlagStatus.OFF)
                .rolloutPercent(request.getRolloutPercent() != null ? request.getRolloutPercent() : 0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        featureFlagRepository.save(flag);

        // create rules if provided
        if (request.getUserGroups() != null) {
            for (String group : request.getUserGroups()) {
                FlagRule rule = FlagRule.builder()
                        .userGroup(group)
                        .condition("{}") // placeholder for now
                        .featureFlag(flag)
                        .build();
                flagRuleRepository.save(rule);
            }
        }

        return toResponse(flag);
    }

    // -------------------------
    // Update Feature Flag
    // -------------------------
    public FeatureFlagResponse updateFlag(Long id, FeatureFlagRequest request) {
        FeatureFlag flag = featureFlagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FeatureFlag not found with id " + id));

        flag.setDescription(request.getDescription());
        flag.setEnvironment(request.getEnvironment());
        flag.setStatus(request.getStatus());
        flag.setRolloutPercent(request.getRolloutPercent());
        flag.setUpdatedAt(LocalDateTime.now());

        // replace rules if new provided
        if (request.getUserGroups() != null) {
            flag.getRules().clear();
            for (String group : request.getUserGroups()) {
                FlagRule rule = FlagRule.builder()
                        .userGroup(group)
                        .condition("{}")
                        .featureFlag(flag)
                        .build();
                flag.getRules().add(rule);
            }
        }

        featureFlagRepository.save(flag);

        return toResponse(flag);
    }

    // -------------------------
    // Toggle Flag ON/OFF
    // -------------------------
    public FeatureFlagResponse toggleFlag(Long id, boolean enable) {
        FeatureFlag flag = featureFlagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FeatureFlag not found with id " + id));

        flag.setStatus(enable ? FlagStatus.ON : FlagStatus.OFF);
        flag.setUpdatedAt(LocalDateTime.now());

        featureFlagRepository.save(flag);

        return toResponse(flag);
    }

    // -------------------------
    // Get all by Environment
    // -------------------------
    public List<FeatureFlagResponse> getAllFlags(Environment env) {
        return featureFlagRepository.findAllByEnvironment(env).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // -------------------------
    // Flag Evaluation for Client
    // -------------------------
    public boolean isFeatureEnabled(String name, Environment env, String userId, String userGroup) {
        FeatureFlag flag = featureFlagRepository.findByNameAndEnvironment(name, env)
                .orElseThrow(() -> new ResourceNotFoundException("FeatureFlag not found with name " + name));

        if (flag.getStatus() == FlagStatus.OFF) {
            return false;
        }

        // Rule check: if userGroup matches any
        if (userGroup != null && flag.getRules() != null) {
            boolean match = flag.getRules().stream()
                    .anyMatch(rule -> userGroup.equals(rule.getUserGroup()));
            if (match) return true;
        }

        // Rollout % check
        if (flag.getRolloutPercent() != null && flag.getRolloutPercent() > 0) {
            int bucket = Math.abs(userId.hashCode() % 100);
            return bucket < flag.getRolloutPercent();
        }

        return true;
    }

    // -------------------------
    // Mapping Helpers
    // -------------------------
    private FeatureFlagResponse toResponse(FeatureFlag flag) {
        FeatureFlagResponse dto = new FeatureFlagResponse();
        dto.setId(flag.getId());
        dto.setName(flag.getName());
        dto.setDescription(flag.getDescription());
        dto.setEnvironment(flag.getEnvironment());
        dto.setStatus(flag.getStatus());
        dto.setRolloutPercent(flag.getRolloutPercent());
        dto.setCreatedAt(flag.getCreatedAt());
        dto.setUpdatedAt(flag.getUpdatedAt());

        if (flag.getRules() != null) {
            List<FlagRuleResponse> rules = flag.getRules().stream()
                    .map(rule -> {
                        FlagRuleResponse r = new FlagRuleResponse();
                        r.setId(rule.getId());
                        r.setUserGroup(rule.getUserGroup());
                        r.setCondition(rule.getCondition());
                        return r;
                    })
                    .collect(Collectors.toList());
            dto.setRules(rules);
        }

        return dto;
    }
}
