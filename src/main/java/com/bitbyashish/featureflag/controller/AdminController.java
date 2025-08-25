package com.bitbyashish.featureflag.controller;

import com.bitbyashish.featureflag.dto.FeatureFlagRequest;
import com.bitbyashish.featureflag.dto.FeatureFlagResponse;
import com.bitbyashish.featureflag.entity.Environment;
import com.bitbyashish.featureflag.service.FeatureFlagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/flags")
@RequiredArgsConstructor
public class AdminController {

    private final FeatureFlagService flagService;

    // Create new feature flag
    @PostMapping
    public ResponseEntity<FeatureFlagResponse> createFlag(@RequestBody FeatureFlagRequest request) {
        return ResponseEntity.ok(flagService.createFlag(request));
    }

    // Update existing feature flag
    @PutMapping("/{id}")
    public ResponseEntity<FeatureFlagResponse> updateFlag(
            @PathVariable Long id,
            @RequestBody FeatureFlagRequest request
    ) {
        return ResponseEntity.ok(flagService.updateFlag(id, request));
    }

    // Toggle ON/OFF
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<FeatureFlagResponse> toggleFlag(
            @PathVariable Long id,
            @RequestParam boolean enable
    ) {
        return ResponseEntity.ok(flagService.toggleFlag(id, enable));
    }

    // Get all flags by environment
    @GetMapping
    public ResponseEntity<List<FeatureFlagResponse>> getAllFlags(@RequestParam Environment env) {
        return ResponseEntity.ok(flagService.getAllFlags(env));
    }
}
