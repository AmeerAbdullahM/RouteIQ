package com.routeiq.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class SystemController {

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }

    @GetMapping("/ready")
    public ResponseEntity<?> ready() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }

    @GetMapping("/providers/status")
    public ResponseEntity<?> providerStatus() {
        return ResponseEntity.ok(Map.of(
            "routing", "AVAILABLE",
            "geocoding", "AVAILABLE",
            "traffic", "CONFIGURED"
        ));
    }
}
