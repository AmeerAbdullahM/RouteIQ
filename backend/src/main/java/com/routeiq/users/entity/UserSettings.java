package com.routeiq.users.entity;

import jakarta.persistence.*;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_settings")
public class UserSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "preferred_mode", nullable = false, length = 30)
    private String preferredMode;

    @Column(name = "default_travel_mode", nullable = false, length = 30)
    private String defaultTravelMode;

    @Column(name = "fuel_efficiency_km_per_litre")
    private Double fuelEfficiencyKmPerLitre;

    @Column(name = "fuel_price_per_litre")
    private Double fuelPricePerLitre;

    @Column(name = "currency_code", nullable = false, length = 3)
    private String currencyCode;

    @Column(name = "distance_unit", nullable = false, length = 10)
    private String distanceUnit;

    @Column(nullable = false, length = 10)
    private String theme;

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
    public String getPreferredMode() { return preferredMode; }
    public void setPreferredMode(String preferredMode) { this.preferredMode = preferredMode; }
    public String getDefaultTravelMode() { return defaultTravelMode; }
    public void setDefaultTravelMode(String defaultTravelMode) { this.defaultTravelMode = defaultTravelMode; }
    public Double getFuelEfficiencyKmPerLitre() { return fuelEfficiencyKmPerLitre; }
    public void setFuelEfficiencyKmPerLitre(Double fuelEfficiencyKmPerLitre) { this.fuelEfficiencyKmPerLitre = fuelEfficiencyKmPerLitre; }
    public Double getFuelPricePerLitre() { return fuelPricePerLitre; }
    public void setFuelPricePerLitre(Double fuelPricePerLitre) { this.fuelPricePerLitre = fuelPricePerLitre; }
    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }
    public String getDistanceUnit() { return distanceUnit; }
    public void setDistanceUnit(String distanceUnit) { this.distanceUnit = distanceUnit; }
    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
    public ZonedDateTime getCreatedAt() { return createdAt; }
    public ZonedDateTime getUpdatedAt() { return updatedAt; }
}
