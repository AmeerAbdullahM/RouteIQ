package com.routeiq.routing.domain;

import com.routeiq.users.entity.User;
import jakarta.persistence.*;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "route_history")
public class RouteHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "request_parameters", nullable = false, columnDefinition = "jsonb")
    private String requestParameters;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "selected_route_summary", columnDefinition = "jsonb")
    private String selectedRouteSummary;

    @Column(name = "provider_name", length = 100)
    private String providerName;

    @Column(name = "traffic_mode", nullable = false, length = 20)
    private String trafficMode;

    @Column(name = "estimate_source", length = 30)
    private String estimateSource;

    @Column(name = "calculation_status", nullable = false, length = 30)
    private String calculationStatus;

    @Column(name = "error_code", length = 100)
    private String errorCode;

    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = ZonedDateTime.now();
    }
    
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getRequestParameters() { return requestParameters; }
    public void setRequestParameters(String requestParameters) { this.requestParameters = requestParameters; }
    public String getSelectedRouteSummary() { return selectedRouteSummary; }
    public void setSelectedRouteSummary(String selectedRouteSummary) { this.selectedRouteSummary = selectedRouteSummary; }
    public String getProviderName() { return providerName; }
    public void setProviderName(String providerName) { this.providerName = providerName; }
    public String getTrafficMode() { return trafficMode; }
    public void setTrafficMode(String trafficMode) { this.trafficMode = trafficMode; }
    public String getEstimateSource() { return estimateSource; }
    public void setEstimateSource(String estimateSource) { this.estimateSource = estimateSource; }
    public String getCalculationStatus() { return calculationStatus; }
    public void setCalculationStatus(String calculationStatus) { this.calculationStatus = calculationStatus; }
    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
    public ZonedDateTime getCreatedAt() { return createdAt; }
}
