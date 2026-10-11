CREATE TABLE saved_routes (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id),
    name VARCHAR(150) NOT NULL,
    origin_name VARCHAR(300) NOT NULL,
    origin_lat DOUBLE PRECISION NOT NULL,
    origin_lon DOUBLE PRECISION NOT NULL,
    destination_name VARCHAR(300) NOT NULL,
    destination_lat DOUBLE PRECISION NOT NULL,
    destination_lon DOUBLE PRECISION NOT NULL,
    optimization_mode VARCHAR(30) NOT NULL,
    travel_mode VARCHAR(30) NOT NULL,
    route_summary TEXT NOT NULL, /* Using TEXT to ensure H2 compatibility, mapped to JSON in JPA */
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
