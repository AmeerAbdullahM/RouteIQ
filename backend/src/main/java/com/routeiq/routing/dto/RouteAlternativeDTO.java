package com.routeiq.routing.dto;

import java.util.List;

public class RouteAlternativeDTO {
    private String id;
    private String name;
    private double distanceKm;
    private long baseDurationSeconds;
    private long adjustedDurationSeconds;
    private Double estimatedFuelCost;
    private Double estimatedEmissionsKg;
    private String explanation;
    private double score;
    private List<double[]> geometryCoordinates; // Array of [lon, lat]

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; }
    public long getBaseDurationSeconds() { return baseDurationSeconds; }
    public void setBaseDurationSeconds(long baseDurationSeconds) { this.baseDurationSeconds = baseDurationSeconds; }
    public long getAdjustedDurationSeconds() { return adjustedDurationSeconds; }
    public void setAdjustedDurationSeconds(long adjustedDurationSeconds) { this.adjustedDurationSeconds = adjustedDurationSeconds; }
    public Double getEstimatedFuelCost() { return estimatedFuelCost; }
    public void setEstimatedFuelCost(Double estimatedFuelCost) { this.estimatedFuelCost = estimatedFuelCost; }
    public Double getEstimatedEmissionsKg() { return estimatedEmissionsKg; }
    public void setEstimatedEmissionsKg(Double estimatedEmissionsKg) { this.estimatedEmissionsKg = estimatedEmissionsKg; }
    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
    public List<double[]> getGeometryCoordinates() { return geometryCoordinates; }
    public void setGeometryCoordinates(List<double[]> geometryCoordinates) { this.geometryCoordinates = geometryCoordinates; }
}
