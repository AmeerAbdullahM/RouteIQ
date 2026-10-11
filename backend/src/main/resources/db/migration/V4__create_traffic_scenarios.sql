CREATE TABLE traffic_scenarios (
    id UUID PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    seed BIGINT NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT FALSE,
    scenario_data TEXT NOT NULL, /* Using TEXT for JSONB compatibility in H2 */
    starts_at TIMESTAMP WITH TIME ZONE,
    expires_at TIMESTAMP WITH TIME ZONE,
    created_by UUID REFERENCES users(id),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
