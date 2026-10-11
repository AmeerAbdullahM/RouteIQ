package com.routeiq.savedroutes.service;

import com.routeiq.savedroutes.entity.SavedRoute;
import com.routeiq.savedroutes.repository.SavedRouteRepository;
import com.routeiq.users.entity.User;
import com.routeiq.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Service
public class SavedRouteService {
    @Autowired
    private SavedRouteRepository savedRouteRepository;

    @Autowired
    private UserRepository userRepository;

    public List<SavedRoute> getUserSavedRoutes(UUID userId) {
        return savedRouteRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public SavedRoute saveRoute(UUID userId, SavedRoute route) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        route.setUser(user);
        return savedRouteRepository.save(route);
    }

    public void deleteRoute(UUID userId, UUID routeId) {
        Optional<SavedRoute> route = savedRouteRepository.findById(routeId);
        if (route.isPresent() && route.get().getUser().getId().equals(userId)) {
            savedRouteRepository.delete(route.get());
        } else {
            throw new RuntimeException("Route not found or unauthorized");
        }
    }
}
