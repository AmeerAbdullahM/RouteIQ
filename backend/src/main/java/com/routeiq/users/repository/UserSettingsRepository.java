package com.routeiq.users.repository;

import com.routeiq.users.entity.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UserSettingsRepository extends JpaRepository<UserSettings, UUID> {
    Optional<UserSettings> findByUserId(UUID userId);
}
