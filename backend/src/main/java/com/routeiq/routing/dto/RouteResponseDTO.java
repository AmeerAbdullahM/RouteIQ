package com.routeiq.routing.dto;

import java.time.ZonedDateTime;
import java.util.List;

public class RouteResponseDTO {
    private List<RouteAlternativeDTO> candidateRoutes;
    private String selectedRouteId;
    private String estimateType;
    private String trafficDataSource;
    private ZonedDateTime calculationTimestamp;

    public List<RouteAlternativeDTO> getCandidateRoutes() { return candidateRoutes; }
    public void setCandidateRoutes(List<RouteAlternativeDTO> candidateRoutes) { this.candidateRoutes = candidateRoutes; }
    public String getSelectedRouteId() { return selectedRouteId; }
    public void setSelectedRouteId(String selectedRouteId) { this.selectedRouteId = selectedRouteId; }
    public String getEstimateType() { return estimateType; }
    public void setEstimateType(String estimateType) { this.estimateType = estimateType; }
    public String getTrafficDataSource() { return trafficDataSource; }
    public void setTrafficDataSource(String trafficDataSource) { this.trafficDataSource = trafficDataSource; }
    public ZonedDateTime getCalculationTimestamp() { return calculationTimestamp; }
    public void setCalculationTimestamp(ZonedDateTime calculationTimestamp) { this.calculationTimestamp = calculationTimestamp; }
}
