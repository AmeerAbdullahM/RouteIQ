package com.routeiq.savedroutes.entity;

import com.routeiq.users.entity.User;
import jakarta.persistence.*;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "saved_routes")
public class SavedRoute {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "origin_name", nullable = false, length = 300)
    private String originName;
    @Column(name = "origin_lat", nullable = false)
    private double originLat;
    @Column(name = "origin_lon", nullable = false)
    private double originLon;

    @Column(name = "destination_name", nullable = false, length = 300)
    private String destinationName;
    @Column(name = "destination_lat", nullable = false)
    private double destinationLat;
    @Column(name = "destination_lon", nullable = false)
    private double destinationLon;

    @Column(name = "optimization_mode", nullable = false, length = 30)
    private String optimizationMode;

    @Column(name = "travel_mode", nullable = false, length = 30)
    private String travelMode;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "route_summary", nullable = false, columnDefinition = "jsonb")
    private String routeSummary;

    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = ZonedDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = ZonedDateTime.now();
    }
    
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getOriginName() { return originName; }
    public void setOriginName(String originName) { this.originName = originName; }
    public double getOriginLat() { return originLat; }
    public void setOriginLat(double originLat) { this.originLat = originLat; }
    public double getOriginLon() { return originLon; }
    public void setOriginLon(double originLon) { this.originLon = originLon; }
    public String getDestinationName() { return destinationName; }
    public void setDestinationName(String destinationName) { this.destinationName = destinationName; }
    public double getDestinationLat() { return destinationLat; }
    public void setDestinationLat(double destinationLat) { this.destinationLat = destinationLat; }
    public double getDestinationLon() { return destinationLon; }
    public void setDestinationLon(double destinationLon) { this.destinationLon = destinationLon; }
    public String getOptimizationMode() { return optimizationMode; }
    public void setOptimizationMode(String optimizationMode) { this.optimizationMode = optimizationMode; }
    public String getTravelMode() { return travelMode; }
    public void setTravelMode(String travelMode) { this.travelMode = travelMode; }
    public String getRouteSummary() { return routeSummary; }
    public void setRouteSummary(String routeSummary) { this.routeSummary = routeSummary; }
    public ZonedDateTime getCreatedAt() { return createdAt; }
    public ZonedDateTime getUpdatedAt() { return updatedAt; }
}
