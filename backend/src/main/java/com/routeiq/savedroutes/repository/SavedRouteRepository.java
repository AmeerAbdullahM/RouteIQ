package com.routeiq.savedroutes.repository;

import com.routeiq.savedroutes.entity.SavedRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface SavedRouteRepository extends JpaRepository<SavedRoute, UUID> {
    List<SavedRoute> findByUserIdOrderByCreatedAtDesc(UUID userId);
}
