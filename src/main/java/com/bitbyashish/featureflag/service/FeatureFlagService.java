package com.bitbyashish.featureflag.service;

import com.bitbyashish.featureflag.entity.*;
import com.bitbyashish.featureflag.repository.*;
import com.bitbyashish.featureflag.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeatureFlagService {

    private final FeatureFlagRepository featureFlagRepository;
    private final FlagRuleRepository flagRuleRepository;
    private final AuditLogRepository auditLogRepository;

    // Create new feature flag
    @Transactional
    public FeatureFlag createFlag(FeatureFlag flag, String performedBy) {
        flag.setCreatedAt(LocalDateTime.now());
        flag.setUpdatedAt(LocalDateTime.now());
        FeatureFlag saved = featureFlagRepository.save(flag);

        logAction(saved, "CREATED", performedBy);
        return saved;
    }

    // Update existing flag
    @Transactional
    public FeatureFlag updateFlag(Long id, FeatureFlag updatedFlag, String performedBy) {
        FeatureFlag flag = featureFlagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FeatureFlag not found: " + id));

        flag.setDescription(updatedFlag.getDescription());
        flag.setStatus(updatedFlag.getStatus());
        flag.setRolloutPercent(updatedFlag.getRolloutPercent());
        flag.setEnvironment(updatedFlag.getEnvironment());
        flag.setUpdatedAt(LocalDateTime.now());

        FeatureFlag saved = featureFlagRepository.save(flag);
        logAction(saved, "UPDATED", performedBy);
        return saved;
    }

    // Toggle ON/OFF
    @Transactional
    public FeatureFlag toggleFlag(Long id, String performedBy) {
        FeatureFlag flag = featureFlagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FeatureFlag not found: " + id));

        flag.setStatus(flag.getStatus() == FlagStatus.ON ? FlagStatus.OFF : FlagStatus.ON);
        flag.setUpdatedAt(LocalDateTime.now());

        FeatureFlag saved = featureFlagRepository.save(flag);
        logAction(saved, "TOGGLED", performedBy);
        return saved;
    }

    // Get all flags
    public List<FeatureFlag> getAllFlags(Environment env) {
        return featureFlagRepository.findAllByEnvironment(env);
    }

    // Get flag by name
    public FeatureFlag getFlagByName(String name, Environment env) {
        return featureFlagRepository.findByNameAndEnvironment(name, env)
                .orElseThrow(() -> new ResourceNotFoundException("FeatureFlag not found: " + name));
    }

    // Evaluate flag for a user (basic logic)
    public boolean evaluateFlag(String name, Environment env, String userId, String userGroup) {
        FeatureFlag flag = getFlagByName(name, env);

        if (flag.getStatus() == FlagStatus.OFF) {
            return false;
        }

        // Check rollout percentage (simple hash-based)
        int hash = Math.abs(userId.hashCode() % 100);
        if (flag.getRolloutPercent() != null && hash >= flag.getRolloutPercent()) {
            return false;
        }

        // Check rules (if user group matches)
        List<FlagRule> rules = flagRuleRepository.findByFeatureFlagId(flag.getId());
        return rules.isEmpty() || rules.stream().anyMatch(rule -> rule.getUserGroup().equalsIgnoreCase(userGroup));
    }

    // Internal audit logging
    private void logAction(FeatureFlag flag, String action, String performedBy) {
        AuditLog log = AuditLog.builder()
                .action(action)
                .performedBy(performedBy)
                .timestamp(LocalDateTime.now())
                .featureFlag(flag)
                .build();
        auditLogRepository.save(log);
    }
}
