package com.routeiq.routing.service;

import com.routeiq.routing.dto.RouteAlternativeDTO;
import com.routeiq.routing.dto.RouteRequestDTO;
import com.routeiq.routing.dto.RouteResponseDTO;
import com.routeiq.routing.provider.RoutingProvider;
import com.routeiq.traffic.provider.TrafficProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Comparator;

@Service
public class RoutingService {

    @Autowired
    private RoutingProvider routingProvider;

    @Autowired
    private TrafficProvider trafficProvider;

    public RouteResponseDTO calculateRoutes(RouteRequestDTO request) {
        // 1. Get base routes
        List<RouteAlternativeDTO> alternatives = routingProvider.calculateRoutes(request);

        // 2. Apply Traffic
        trafficProvider.applyTraffic(alternatives, request.getTrafficMode());

        // 3. Apply Optimization Scoring
        rankRoutes(alternatives, request.getOptimizationMode());

        // 4. Construct Response
        RouteResponseDTO response = new RouteResponseDTO();
        response.setCandidateRoutes(alternatives);
        
        if (!alternatives.isEmpty()) {
            response.setSelectedRouteId(alternatives.get(0).getId());
        }
        
        response.setEstimateType(request.getTrafficMode());
        response.setTrafficDataSource(request.getTrafficMode());
        response.setCalculationTimestamp(ZonedDateTime.now());

        return response;
    }

    private void rankRoutes(List<RouteAlternativeDTO> routes, String optimizationMode) {
        for (RouteAlternativeDTO route : routes) {
            double score = 0;
            switch (optimizationMode.toUpperCase()) {
                case "FASTEST":
                    score = route.getAdjustedDurationSeconds();
                    route.setExplanation("Selected based on fastest travel time.");
                    break;
                case "SHORTEST":
                    score = route.getDistanceKm();
                    route.setExplanation("Selected based on shortest distance.");
                    break;
                case "FUEL":
                    // Simple mock for fuel cost - proportional to distance
                    score = route.getDistanceKm() * 1.5; 
                    route.setEstimatedFuelCost(score);
                    route.setExplanation("Selected based on lowest estimated fuel cost.");
                    break;
                case "BALANCED":
                default:
                    // 60% time, 40% distance (normalized roughly)
                    score = (route.getAdjustedDurationSeconds() / 60.0) * 0.6 + route.getDistanceKm() * 0.4;
                    route.setExplanation("Selected based on balanced time and distance.");
                    break;
            }
            route.setScore(score);
        }

        routes.sort(Comparator.comparingDouble(RouteAlternativeDTO::getScore));
    }
}
