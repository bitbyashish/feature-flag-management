package com.bitbyashish.featureflag.controller;

import com.bitbyashish.featureflag.entity.Environment;
import com.bitbyashish.featureflag.service.FeatureFlagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client/flags")
@RequiredArgsConstructor
public class ClientController {

    private final FeatureFlagService flagService;

    // Check if feature is enabled for a user
    @GetMapping("/{name}/check")
    public ResponseEntity<Boolean> isFeatureEnabled(
            @PathVariable String name,
            @RequestParam Environment env,
            @RequestParam String userId,
            @RequestParam(required = false) String userGroup
    ) {
        boolean enabled = flagService.isFeatureEnabled(name, env, userId, userGroup);
        return ResponseEntity.ok(enabled);
    }
}

