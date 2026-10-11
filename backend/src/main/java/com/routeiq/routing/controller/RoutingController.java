package com.routeiq.routing.controller;

import com.routeiq.routing.dto.RouteRequestDTO;
import com.routeiq.routing.dto.RouteResponseDTO;
import com.routeiq.routing.service.RoutingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/routes")
public class RoutingController {

    @Autowired
    private RoutingService routingService;

    @PostMapping("/calculate")
    public ResponseEntity<RouteResponseDTO> calculateRoute(@Valid @RequestBody RouteRequestDTO request) {
        return ResponseEntity.ok(routingService.calculateRoutes(request));
    }
}
