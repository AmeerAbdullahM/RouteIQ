package com.routeiq.traffic.domain;

import com.routeiq.users.entity.User;
import jakarta.persistence.*;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "traffic_scenarios")
public class TrafficScenario {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private long seed;

    @Column(nullable = false)
    private boolean enabled = false;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "scenario_data", nullable = false, columnDefinition = "jsonb")
    private String scenarioData;

    @Column(name = "starts_at")
    private ZonedDateTime startsAt;

    @Column(name = "expires_at")
    private ZonedDateTime expiresAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

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
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public long getSeed() { return seed; }
    public void setSeed(long seed) { this.seed = seed; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public String getScenarioData() { return scenarioData; }
    public void setScenarioData(String scenarioData) { this.scenarioData = scenarioData; }
    public ZonedDateTime getStartsAt() { return startsAt; }
    public void setStartsAt(ZonedDateTime startsAt) { this.startsAt = startsAt; }
    public ZonedDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(ZonedDateTime expiresAt) { this.expiresAt = expiresAt; }
    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }
    public ZonedDateTime getCreatedAt() { return createdAt; }
    public ZonedDateTime getUpdatedAt() { return updatedAt; }
}
