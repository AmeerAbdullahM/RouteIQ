package com.routeiq.traffic.provider;

import com.routeiq.traffic.domain.TrafficScenario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface TrafficScenarioRepository extends JpaRepository<TrafficScenario, UUID> {
}
