package com.routeiq.geocoding.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

public class LocationDTO {
    @NotBlank
    private String name;

    @NotNull
    @Min(-90)
    @Max(90)
    private Double latitude;

    @NotNull
    @Min(-180)
    @Max(180)
    private Double longitude;

    private String displayName;
    private String details;

    public LocationDTO() {}

    public LocationDTO(String name, Double latitude, Double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocationDTO(String name, Double latitude, Double longitude, String displayName, String details) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.displayName = displayName;
        this.details = details;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public String getDisplayName() { return displayName != null ? displayName : name; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}
