package com.bitbyashish.featureflag.controller;

import com.bitbyashish.featureflag.entity.Environment;
import com.bitbyashish.featureflag.entity.FeatureFlag;
import com.bitbyashish.featureflag.service.FeatureFlagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/flags")
@RequiredArgsConstructor
public class AdminController {

    private final FeatureFlagService featureFlagService;

    // Create new flag
    @PostMapping
    public ResponseEntity<FeatureFlag> createFlag(
            @RequestBody FeatureFlag flag,
            @RequestParam(defaultValue = "system") String performedBy) {
        return ResponseEntity.ok(featureFlagService.createFlag(flag, performedBy));
    }

    // Update flag
    @PutMapping("/{id}")
    public ResponseEntity<FeatureFlag> updateFlag(
            @PathVariable Long id,
            @RequestBody FeatureFlag flag,
            @RequestParam(defaultValue = "system") String performedBy) {
        return ResponseEntity.ok(featureFlagService.updateFlag(id, flag, performedBy));
    }

    // Toggle ON/OFF
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<FeatureFlag> toggleFlag(
            @PathVariable Long id,
            @RequestParam(defaultValue = "system") String performedBy) {
        return ResponseEntity.ok(featureFlagService.toggleFlag(id, performedBy));
    }

    // Get all flags for environment
    @GetMapping
    public ResponseEntity<List<FeatureFlag>> getAllFlags(
            @RequestParam(defaultValue = "DEV") Environment environment) {
        return ResponseEntity.ok(featureFlagService.getAllFlags(environment));
    }

    // Get flag by name
    @GetMapping("/{name}")
    public ResponseEntity<FeatureFlag> getFlagByName(
            @PathVariable String name,
            @RequestParam(defaultValue = "DEV") Environment environment) {
        return ResponseEntity.ok(featureFlagService.getFlagByName(name, environment));
    }
}
