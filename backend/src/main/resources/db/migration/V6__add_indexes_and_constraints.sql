-- Indexes for users
CREATE INDEX idx_users_email ON users(email);

-- Indexes for saved routes
CREATE INDEX idx_saved_routes_user_id ON saved_routes(user_id);

-- Indexes for incidents
CREATE INDEX idx_incidents_status ON incidents(status);
CREATE INDEX idx_incidents_type ON incidents(incident_type);

-- Indexes for route history
CREATE INDEX idx_route_history_user_id ON route_history(user_id);
CREATE INDEX idx_route_history_created_at ON route_history(created_at);

-- Constraints
ALTER TABLE saved_routes ADD CONSTRAINT chk_saved_routes_lat_lon CHECK (
    origin_lat BETWEEN -90 AND 90 AND
    origin_lon BETWEEN -180 AND 180 AND
    destination_lat BETWEEN -90 AND 90 AND
    destination_lon BETWEEN -180 AND 180
);

ALTER TABLE incidents ADD CONSTRAINT chk_incidents_lat_lon CHECK (
    latitude BETWEEN -90 AND 90 AND
    longitude BETWEEN -180 AND 180
);
