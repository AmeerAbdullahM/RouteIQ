CREATE TABLE users (
    id UUID PRIMARY KEY,
    email VARCHAR(320) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    display_name VARCHAR(120) NOT NULL,
    role VARCHAR(20) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE user_settings (
    id UUID PRIMARY KEY,
    user_id UUID UNIQUE NOT NULL REFERENCES users(id),
    preferred_mode VARCHAR(30) NOT NULL,
    default_travel_mode VARCHAR(30) NOT NULL,
    fuel_efficiency_km_per_litre NUMERIC(10,3),
    fuel_price_per_litre NUMERIC(12,3),
    currency_code CHAR(3) NOT NULL,
    distance_unit VARCHAR(10) NOT NULL,
    theme VARCHAR(10) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
