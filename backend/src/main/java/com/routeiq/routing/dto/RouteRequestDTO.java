package com.routeiq.routing.dto;

import com.routeiq.geocoding.dto.LocationDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class RouteRequestDTO {
    @NotNull
    @Valid
    private LocationDTO origin;

    @NotNull
    @Valid
    private LocationDTO destination;

    @NotNull
    private String travelMode; // DRIVING, WALKING, CYCLING

    @NotNull
    private String optimizationMode; // FASTEST, SHORTEST, BALANCED, FUEL

    @NotNull
    private String trafficMode; // SIMULATED, LIVE, HYBRID

    private boolean avoidTolls;
    private boolean avoidClosures;

    public LocationDTO getOrigin() { return origin; }
    public void setOrigin(LocationDTO origin) { this.origin = origin; }
    public LocationDTO getDestination() { return destination; }
    public void setDestination(LocationDTO destination) { this.destination = destination; }
    public String getTravelMode() { return travelMode; }
    public void setTravelMode(String travelMode) { this.travelMode = travelMode; }
    public String getOptimizationMode() { return optimizationMode; }
    public void setOptimizationMode(String optimizationMode) { this.optimizationMode = optimizationMode; }
    public String getTrafficMode() { return trafficMode; }
    public void setTrafficMode(String trafficMode) { this.trafficMode = trafficMode; }
    public boolean isAvoidTolls() { return avoidTolls; }
    public void setAvoidTolls(boolean avoidTolls) { this.avoidTolls = avoidTolls; }
    public boolean isAvoidClosures() { return avoidClosures; }
    public void setAvoidClosures(boolean avoidClosures) { this.avoidClosures = avoidClosures; }
}
