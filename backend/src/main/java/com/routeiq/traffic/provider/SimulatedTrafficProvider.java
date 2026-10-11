package com.routeiq.traffic.provider;

import com.routeiq.routing.dto.RouteAlternativeDTO;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Random;

@Service
public class SimulatedTrafficProvider implements TrafficProvider {

    @Override
    public void applyTraffic(List<RouteAlternativeDTO> routes, String trafficMode) {
        if ("LIVE".equalsIgnoreCase(trafficMode)) {
            throw new RuntimeException("Live traffic provider is not configured. Falling back to simulated or none.");
        }

        if (!"SIMULATED".equalsIgnoreCase(trafficMode) && !"HYBRID".equalsIgnoreCase(trafficMode)) {
            return;
        }

        // Apply a deterministic but pseudo-random delay based on route geometry/name for demonstration
        for (RouteAlternativeDTO route : routes) {
            Random rand = new Random(route.getName().hashCode());
            // Add up to 30% delay
            double delayFactor = 1.0 + (rand.nextDouble() * 0.30);
            long adjusted = (long) (route.getBaseDurationSeconds() * delayFactor);
            route.setAdjustedDurationSeconds(adjusted);
        }
    }
}
