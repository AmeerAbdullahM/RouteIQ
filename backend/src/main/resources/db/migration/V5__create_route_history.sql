CREATE TABLE route_history (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id),
    request_parameters TEXT NOT NULL, /* JSON representation */
    selected_route_summary TEXT, /* JSON representation */
    provider_name VARCHAR(100),
    traffic_mode VARCHAR(20) NOT NULL,
    estimate_source VARCHAR(30),
    calculation_status VARCHAR(30) NOT NULL,
    error_code VARCHAR(100),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);
