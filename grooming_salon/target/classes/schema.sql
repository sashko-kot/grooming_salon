CREATE TABLE IF NOT EXISTS grooming_services (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    duration_minutes INT NOT NULL CHECK (duration_minutes > 0),
    price DECIMAL(10, 2) NOT NULL CHECK (price >= 0),
    description VARCHAR(1000) NOT NULL
);

CREATE TABLE IF NOT EXISTS appointments (
    id BIGSERIAL PRIMARY KEY,
    client_name VARCHAR(255) NOT NULL,
    pet_name VARCHAR(255) NOT NULL,
    service_id BIGINT NOT NULL REFERENCES grooming_services(id) ON DELETE CASCADE,
    booking_code VARCHAR(50) NOT NULL UNIQUE,
    status VARCHAR(50) NOT NULL DEFAULT 'NEW'
);