package com.routeiq.incidents.repository;

import com.routeiq.incidents.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface IncidentRepository extends JpaRepository<Incident, UUID> {
}
