package com.routeiq.savedroutes.controller;

import com.routeiq.savedroutes.entity.SavedRoute;
import com.routeiq.savedroutes.service.SavedRouteService;
import com.routeiq.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/saved-routes")
public class SavedRouteController {

    @Autowired
    private SavedRouteService savedRouteService;

    @GetMapping
    public ResponseEntity<List<SavedRoute>> getSavedRoutes(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(savedRouteService.getUserSavedRoutes(userDetails.getId()));
    }

    @PostMapping
    public ResponseEntity<SavedRoute> createSavedRoute(@RequestBody SavedRoute savedRoute, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return ResponseEntity.ok(savedRouteService.saveRoute(userDetails.getId(), savedRoute));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSavedRoute(@PathVariable UUID id, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        savedRouteService.deleteRoute(userDetails.getId(), id);
        return ResponseEntity.ok().build();
    }
}
